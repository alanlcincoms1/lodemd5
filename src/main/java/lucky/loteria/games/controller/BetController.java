package lucky.loteria.games.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.sentry.Sentry;
import lucky.loteria.games.external_dto.response.BetResponse;
import lucky.loteria.games.internal_dto.BetForm;
import lucky.loteria.games.model.Bet;
import lucky.loteria.games.model.Table;
import lucky.loteria.games.model.redis.ConfigurationRedis;
import lucky.loteria.games.model.redis.UserRedis;
import lucky.loteria.games.repository.impl.BetRepository;
import lucky.loteria.games.repository.impl.TableRepository;
import lucky.loteria.games.services.ConfigurationService;
import lucky.loteria.games.services.UserService;
import lucky.loteria.games.services.game_core.DataResults;
import lucky.loteria.games.services.game_core.GameAbstract;
import lucky.loteria.games.services.game_core.GameFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


@RestController
@RequestMapping("api/v1/")
public class BetController extends ExceptionHandle {

    private final BetRepository betRepository;

    private final UserService userService;

    private final TableRepository tableRepository;

    private final Gson gson;

    private final GameFactory gameFactory;

    private final ConfigurationService configurationService;

    private static final Gson serializerNull = new GsonBuilder().serializeNulls().create();

    public BetController(BetRepository betRepository,
                         UserService userService,
                         TableRepository tableRepository, Gson gson, GameFactory gameFactory,
                         ConfigurationService configurationService) {
        this.betRepository = betRepository;
        this.userService = userService;
        this.tableRepository = tableRepository;
        this.gson = gson;
        this.gameFactory = gameFactory;
        this.configurationService = configurationService;
    }

    @PostMapping(value = "bet", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Object> bet(@RequestBody BetForm betForm, HttpServletRequest httpServletRequest) {
        Sentry.getContext().addExtra("requestAPI", betForm);
        UserRedis user = userService.getUserByToken(betForm.getToken(), httpServletRequest);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Vui lòng đăng nhập để quay!");
        }

        Table table = tableRepository.findTableByIdEquals(betForm.getTableId());
        GameAbstract gameAbstract = gameFactory.getInstance(table.getGroupName());

        Bet bet = createBet(httpServletRequest, user, table, betForm.getBetAmount());

        ConfigurationRedis configurationRedis = configurationService.getConfig(bet.getTableId());
        DataResults dataResults = gameAbstract.createRandomResult(table, bet, configurationRedis, betForm.getRound());
        gameAbstract.updateBetAfterResult(bet, dataResults, user.getUsername());

        betRepository.save(bet);
        BetResponse betResponse = BetResponse.builder()
                .round(bet.getAlphabet())
                .prize(dataResults.getPrize())
                .username(user.getUsername())
                .build();
        return new ResponseEntity<>(serializerNull.toJson(betResponse), HttpStatus.OK);
    }


    private Bet createBet(HttpServletRequest httpServletRequest, UserRedis user, Table table, Double amount) {
        Bet bet = new Bet();
        bet.setAmount(amount);
        bet.setIp(httpServletRequest.getRemoteAddr());
        bet.setUid(user.getUid());
        bet.setMemberId(user.getMember_id());
        bet.setTableId(table.getId());
        bet.setStatus(Bet.BetStatus.BET.name());
        bet.setAgentString(httpServletRequest.getHeader("User-Agent"));
        bet.setIsRunning(Bet.RUNNING_STATUS.RUNNING.getValue());
        bet.setUsername(user.getUsername());
        bet.setCreatedDate(new Date());
        bet.setUpdatedDate(new Date());
        betRepository.save(bet);
        return bet;
    }
}
