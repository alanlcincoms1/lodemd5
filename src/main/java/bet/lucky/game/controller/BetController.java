package bet.lucky.game.controller;

import bet.lucky.game.exception.ApplicationException;
import bet.lucky.game.exception.message.BetMessage;
import bet.lucky.game.external_dto.response.BetResponse;
import bet.lucky.game.external_dto.response.BetResponseDto;
import bet.lucky.game.internal_dto.BetForm;
import bet.lucky.game.internal_dto.BetHistoriesForm;
import bet.lucky.game.model.*;
import bet.lucky.game.model.redis.ConfigurationRedis;
import bet.lucky.game.model.redis.UserRedis;
import bet.lucky.game.repository.impl.BetRepository;
import bet.lucky.game.repository.impl.TableRepository;
import bet.lucky.game.repository.impl.TransactionRepository;
import bet.lucky.game.repository.impl.UserRedisRepository;
import bet.lucky.game.services.BetService;
import bet.lucky.game.services.ConfigurationService;
import bet.lucky.game.services.UserService;
import bet.lucky.game.services.game_core.DataResults;
import bet.lucky.game.services.game_core.GameAbstract;
import bet.lucky.game.services.game_core.GameFactory;
import bet.lucky.game.utils.GameUtils;
import bet.lucky.game.utils.Utilities;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/")
public class BetController {
    private static final int MAX_LIMIT = 60;
    public static final int MAX_PAGE = 20;

    private final BetRepository betRepository;

    private final TransactionRepository transactionRepository;

    private final UserService userService;

    private final UserRedisRepository userRedisRepository;

    private final TableRepository tableRepository;

    private final Gson gson;

    private final GameFactory gameFactory;

    private final BetService betService;

    private final ConfigurationService configurationService;

    private static final Gson serializerNull = new GsonBuilder().serializeNulls().create();

    @PostMapping(value = "bet", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Object> bet(@RequestBody BetForm betForm, HttpServletRequest httpServletRequest) {

        UserRedis user = userService.getUserByToken(betForm.getToken(), httpServletRequest);
        DataResults dataResults = null;
        try {
            Table table = tableRepository.findTableByIdEquals(betForm.getTableId());
            GameAbstract gameAbstract = gameFactory.getInstance(table.getGroupName());

            Bet bet = createBet(httpServletRequest, user, table, betForm.getBetAmount());

//        callServiceAfterBet(bet);

            ConfigurationRedis configurationRedis = configurationService.getConfig(bet.getTableId());
            dataResults = gameAbstract.createRandomResult(table, bet, configurationRedis);
            gameAbstract.updateBetAfterResult(bet, dataResults, user.getUsername(), betForm.getBetAmount());

            betRepository.save(bet);
        } catch (Exception ex) {
            Utilities.LOGGER.error(ex.getMessage());
            throw new ApplicationException(BetMessage.INVALID_PARAMETER);
        }

        BetResponse betResponse = BetResponse.builder()
                .username(user.getUsername())
                .reel(dataResults.getResult().getReel())
                .prize(dataResults.getResult().getPrize())
                .build();
        return new ResponseEntity<>(serializerNull.toJson(betResponse), HttpStatus.OK);
    }

    @GetMapping(value = "bet/gettop", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BetResponseDto> getTopBet(@PathParam("token") String token) {
        List<BetResponseDto> responseList = betService.getTopBet(token);
        return responseList;
    }

    private void callServiceAfterBet(Bet bet) {
        betService.transactionBet(bet);
    }

    @GetMapping(value = "user-bet/histories", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> histories(@ModelAttribute BetHistoriesForm betHistoriesForm) {
        if (betHistoriesForm.getLimit() > MAX_LIMIT) betHistoriesForm.setLimit(MAX_LIMIT);
        if (betHistoriesForm.getPage() > MAX_PAGE) betHistoriesForm.setLimit(MAX_PAGE);
        Pageable sortedByPriceDescNameAsc = PageRequest.of(betHistoriesForm.getPage(),
                betHistoriesForm.getLimit(),
                Sort.by("id").descending());
        Page<Bet> bets = betRepository.findAllByTableIdAndStatusIn(
                betHistoriesForm.getTableId(),
                new String[]{Bet.BetStatus.DRAW.name(), Bet.BetStatus.WIN.name()},
                sortedByPriceDescNameAsc);

        if (!bets.hasContent()) return new ResponseEntity<>(gson.toJson(bets), HttpStatus.OK);

        PageImpl result = new PageImpl<>(
                bets.getContent().stream().map(b -> BetResponse.builder().username(GameUtils.convertUS(b.getUsername()))
                        .prize(b.getAmountWin())
                ).collect(Collectors.toList()),
                sortedByPriceDescNameAsc, bets.getTotalElements());
        return new ResponseEntity<>(gson.toJson(result), HttpStatus.OK);
    }

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

    @GetMapping(value = "update-bet/{id}")
    @ResponseBody
    public ResponseEntity<String> updateBets(@PathVariable Integer id) {
        Bet bet = betRepository.getOne(id);
        List<Transaction> transactions = null;
        if ((Bet.BetStatus.WIN.name().equals(bet.getStatus()) || Bet.BetStatus.LOSE.name().equals(bet.getStatus()) || Bet.BetStatus.DRAW.name().equals(bet.getStatus()))) {
            transactions = transactionRepository.findTransactionsByBetId(bet.getId());
            if (transactions != null && transactions.size() > 0) {
                Transaction lastedTransaction = transactions.get(0);
                if (!Transaction.TransactionStatus.SUCCESS.name().equals(lastedTransaction.getStatus())) {
//					bet.setIsRunning(Bet.RUNNING_STATUS.RUNNING.getValue());
                    betRepository.save(bet);
                } else {
                    return new ResponseEntity<>("Bet này đã update rồi : transaction_id: " +
                            lastedTransaction.getTransactionHash() + "." + lastedTransaction.getId() + " agent_transaction: " + lastedTransaction.getAgentTransactionId()
                            + " Status: " + lastedTransaction.getType(), HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>("Bet này chưa thành công", HttpStatus.PAYMENT_REQUIRED);
            }
        } else {
            return new ResponseEntity<>("Bet này chưa thành công !", HttpStatus.PAYMENT_REQUIRED);
        }

        if (transactions.get(0).getNote() != null && transactions.get(0).getNote().contains("message=User is locked, error_code=606")) {
            return new ResponseEntity<>("Block User " + transactions.get(0).getNote(), HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>("Update is success -> trạng thái trước đó là: " + transactions.get(0).getNote(), HttpStatus.OK);
    }

    private Bet createBet(HttpServletRequest httpServletRequest, UserRedis user, Table table, Double betAmount) {
        Bet bet = new Bet();
        bet.setIp(httpServletRequest.getRemoteAddr());
        bet.setUid(user.getUid());
        bet.setMemberId(user.getMember_id());
        bet.setTableId(table.getId());
        bet.setAmount(betAmount);
        bet.setStatus(Bet.BetStatus.BET.name());
        bet.setAgentString(httpServletRequest.getHeader("User-Agent"));
        bet.setIsRunning(Bet.RUNNING_STATUS.RUNNING.getValue());
        bet.setUsername(user.getUsername());
        bet.setCreatedDate(new Date());
        betRepository.save(bet);
        return bet;
    }
}