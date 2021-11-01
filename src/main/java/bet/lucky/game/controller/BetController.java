package bet.lucky.game.controller;

import bet.lucky.game.exception.ApplicationException;
import bet.lucky.game.exception.message.BetMessage;
import bet.lucky.game.external_dto.request.BetHistoryRequest;
import bet.lucky.game.external_dto.response.BetResponse;
import bet.lucky.game.external_dto.response.BetTopResponse;
import bet.lucky.game.internal_dto.BetForm;
import bet.lucky.game.internal_dto.BetHistoriesForm;
import bet.lucky.game.model.Bet;
import bet.lucky.game.model.BetStatement;
import bet.lucky.game.model.IBetStatement;
import bet.lucky.game.model.Tables;
import bet.lucky.game.model.redis.ConfigurationRedis;
import bet.lucky.game.model.redis.UserRedis;
import bet.lucky.game.repository.impl.BetRepository;
import bet.lucky.game.repository.impl.TableRepository;
import bet.lucky.game.repository.impl.UserRedisRepository;
import bet.lucky.game.services.BetService;
import bet.lucky.game.services.ConfigurationService;
import bet.lucky.game.services.UserService;
import bet.lucky.game.services.game_core.DataResults;
import bet.lucky.game.services.game_core.GameAbstract;
import bet.lucky.game.services.game_core.GameFactory;
import bet.lucky.game.utils.ResponseFactory;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/")
@Slf4j
public class BetController {
    private static final int MAX_LIMIT = 60;
    public static final int MAX_PAGE = 20;

    private final BetRepository betRepository;

    private final UserService userService;

    private final UserRedisRepository userRedisRepository;

    private final TableRepository tableRepository;

    private final Gson gson;

    private final GameFactory gameFactory;

    private final BetService betService;

    private final ConfigurationService configurationService;

    @PostMapping(value = "bet", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<BetResponse> bet(@RequestBody BetForm betForm, HttpServletRequest httpServletRequest) {

        UserRedis user = userService.getUserByToken(betForm.getToken(), httpServletRequest);
        DataResults dataResults;
        Bet bet;
        BetResponse betResponse;
        try {
            Tables tables = tableRepository.findTableByIdEquals(betForm.getTableId());
            GameAbstract gameAbstract = gameFactory.getInstance(tables.getGroupName());

            bet = createBet(httpServletRequest, user, tables, betForm.getBetAmount());

            ConfigurationRedis configurationRedis = configurationService.getConfig(bet.getTableId());
            dataResults = gameAbstract.createRandomResult(tables, bet, configurationRedis);
            betResponse = gameAbstract.updateBetAfterResult(tables, bet, dataResults, user.getFullname(), betForm.getBetAmount());
            betRepository.save(bet);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("bet lỗi: " + ex.getMessage());
            throw new ApplicationException(BetMessage.INVALID_PARAMETER);
        }
        betResponse.setUsername(user.getFullname());
        return ResponseFactory.success(betResponse);
    }

    @GetMapping(value = "bet/gettop", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BetTopResponse> getTopBet() {
        List<BetTopResponse> responseList = betService.findTopBet();
        return responseList;
    }

    @PostMapping(value = "bet/history", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> getHistory(@RequestBody BetHistoryRequest request, HttpServletRequest httpServletRequest) {
        Map<String, Object> responseMap = betService.getPageBetHistory(request);
        return responseMap;
    }


    //=================***********************========================

    @GetMapping(value = "user-bet/openbet")
    @ResponseBody
    public ResponseEntity<String> betUser(@ModelAttribute BetHistoriesForm betHistoriesForm) {
        Optional<UserRedis> userRedisOptional = userRedisRepository.findById(betHistoriesForm.getToken());

        if (userRedisOptional.isEmpty()) {
            return new ResponseEntity<>(gson.toJson("Vui lòng đăng nhập!"), HttpStatus.BAD_REQUEST);
        }

        List<Bet> bets = betRepository.findAllByStatusInAndIsRunningEqualsAndUidEquals(
                new String[]{Bet.BetStatus.BET.name()},
                Bet.RUNNING_STATUS.RUNNING.getValue(),
                userRedisOptional.get().getUid()
        );

        return new ResponseEntity<>(gson.toJson(bets), HttpStatus.OK);
    }

    @GetMapping(value = "user-bet/dailystatement")
    @ResponseBody
    public ResponseEntity<String> dailyStatement(@ModelAttribute BetHistoriesForm betHistoriesForm) {
        Optional<UserRedis> userRedisOptional = userRedisRepository.findById(betHistoriesForm.getToken());

        if (userRedisOptional.isEmpty()) {
            return new ResponseEntity<>(gson.toJson("Vui lòng đăng nhập!"), HttpStatus.BAD_REQUEST);
        }
        List<BetStatement> betStatementList = new ArrayList<>();
        List<IBetStatement> bets = betRepository.dailyStatement(
                userRedisOptional.get().getUid()
        );
        for (IBetStatement iBet : bets) {
            BetStatement betStatement = new BetStatement();
            betStatement.setAmountTotal(iBet.getAmountTotal());
            betStatement.setAmountWinTotal(iBet.getAmountWinTotal());
            betStatement.setBetDate(iBet.getBetDate());
            betStatementList.add(betStatement);
        }
        return new ResponseEntity<>(gson.toJson(betStatementList), HttpStatus.OK);
    }

    @GetMapping(value = "user-bet/dailystatement-detail")
    @ResponseBody
    public ResponseEntity<String> dailyStatementDetail(@ModelAttribute BetHistoriesForm betHistoriesForm) {
        Optional<UserRedis> userRedisOptional = userRedisRepository.findById(betHistoriesForm.getToken());

        if (userRedisOptional.isEmpty()) {
            return new ResponseEntity<>(gson.toJson("Vui lòng đăng nhập!"), HttpStatus.BAD_REQUEST);
        }

        if (betHistoriesForm.getDate() == null) {
            String pattern = "yyyy/MM/dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

            String date = simpleDateFormat.format(new Date());
            betHistoriesForm.setDate(new Date(date));
        }
        betHistoriesForm.setToDate(new Date(betHistoriesForm.getDate().getTime() + 86400000));
        Pageable paging = PageRequest.of(betHistoriesForm.getPage(), betHistoriesForm.getLimit(), Sort.by("id").descending());
        Page<Bet> bets = betRepository.findAllByCreatedDateGreaterThanAndUidEqualsAndCreatedDateLessThanEqualAndStatusIsNot(
                betHistoriesForm.getDate(),
                userRedisOptional.get().getUid(),
                betHistoriesForm.getToDate(),
                Bet.BetStatus.NONE.name(),
                paging
        );

        return new ResponseEntity<>(gson.toJson(bets), HttpStatus.OK);
    }


    private Bet createBet(HttpServletRequest httpServletRequest, UserRedis user, Tables tables, Double betAmount) {
        Bet bet = new Bet();
        bet.setIp(httpServletRequest.getRemoteAddr());
        bet.setUid(user.getUid());
        bet.setMemberId(user.getMember_id());
        bet.setTableId(tables.getId());
        bet.setAmount(betAmount);
        bet.setStatus(Bet.BetStatus.BET.name());
        bet.setIsRunning(Bet.RUNNING_STATUS.RUNNING.getValue());
        bet.setCreatedDate(new Date());
        bet.setFullname(user.getFullname());
        bet.setAgencyId(user.getAgency_id());
        bet.setTransactionHash(System.currentTimeMillis() + "");
        betRepository.save(bet);
        return bet;
    }
}
