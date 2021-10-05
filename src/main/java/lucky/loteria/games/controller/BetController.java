package lucky.loteria.games.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lucky.loteria.games.external_dto.response.BetResponseDto;
import lucky.loteria.games.external_dto.response.BetResponse;
import lucky.loteria.games.internal_dto.BetForm;
import lucky.loteria.games.internal_dto.BetHistoriesForm;
import lucky.loteria.games.model.Bet;
import lucky.loteria.games.model.BetStatement;
import lucky.loteria.games.model.IBetStatement;
import lucky.loteria.games.model.Table;
import lucky.loteria.games.model.Transaction;
import lucky.loteria.games.model.redis.ConfigurationRedis;
import lucky.loteria.games.model.redis.UserRedis;
import lucky.loteria.games.repository.impl.BetRepository;
import lucky.loteria.games.repository.impl.TableRepository;
import lucky.loteria.games.repository.impl.TransactionRepository;
import lucky.loteria.games.repository.impl.UserRedisRepository;
import lucky.loteria.games.services.BetService;
import lucky.loteria.games.services.ConfigurationService;
import lucky.loteria.games.services.UserService;
import lucky.loteria.games.services.game_core.DataResults;
import lucky.loteria.games.services.game_core.GameAbstract;
import lucky.loteria.games.services.game_core.GameFactory;
import lucky.loteria.games.utils.GameUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
public class BetController extends ExceptionHandle {
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
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Vui lòng đăng nhập để quay!");
        }

        Table table = tableRepository.findTableByIdEquals(betForm.getTableId());
        GameAbstract gameAbstract = gameFactory.getInstance(table.getGroupName());

        Bet bet = createBet(httpServletRequest, user, table, betForm.getBetAmount());

//        callServiceAfterBet(bet);

        ConfigurationRedis configurationRedis = configurationService.getConfig(bet.getTableId());
        DataResults dataResults = gameAbstract.createRandomResult(table, bet, configurationRedis);
        gameAbstract.updateBetAfterResult(bet, dataResults, user.getUsername(), betForm.getBetAmount());

        betRepository.save(bet);
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
