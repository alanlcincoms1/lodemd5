package lucky.loteria.games.services.game_instance;

import com.google.gson.Gson;
import lucky.loteria.games.constance.Constance;
import lucky.loteria.games.model.*;
import lucky.loteria.games.model.redis.ConfigurationRedis;
import lucky.loteria.games.repository.impl.BetRepository;
import lucky.loteria.games.repository.impl.TableRepository;
import lucky.loteria.games.services.ConfigurationService;
import lucky.loteria.games.services.SocketService;
import lucky.loteria.games.services.UserService;
import lucky.loteria.games.services.game_core.DataResults;
import lucky.loteria.games.services.game_core.GameAbstract;
import lucky.loteria.games.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class GameLucky extends GameAbstract {
    public static final String GROUP_NAME = Constance.GROUP_NAME_LUCKY;
    public static final List<String> ROUND = List.of("A", "B");
    final BetRepository betRepository;

    @Autowired
    UserService userService;

    @Autowired
    ConfigurationService configurationService;

    @Autowired
    SocketService socketService;


    @Autowired
    public GameLucky(
            TableRepository tableRepository,
            BetRepository betRepository,
            SocketService socketService,
            Gson gson) {
        super(tableRepository, betRepository, socketService, gson);
        groupName = GameLucky.GROUP_NAME;
        this.betRepository = betRepository;
    }

    @Override
    public DataResults createRandomResult(Table table, Bet bet, ConfigurationRedis configurationRedis, String round) {
        HashMap<String, Config> configHashMap = configurationRedis.getCollection();
        List<Double> lisRandom = configHashMap.get(round).getListPrize();
        Double numRandom = RandomUtils.getRandomFromList(lisRandom);

        DataResults dataResult = new DataResults();
        dataResult.setResult(configHashMap.get(round));
        dataResult.setKey(round);
        dataResult.setPrize(numRandom);

        return dataResult;
    }

    @Override
    public boolean isValidBetType(String betTypeString) {
        return true;
    }

    public void updateBetAfterResult(Bet bet, DataResults dataResults, String username) {
        bet.setAlphabet(dataResults.getKey());
        if (dataResults.isLose()) {
            bet.setStatus(Bet.BetStatus.LOSE.name());
            bet.setAmountWin(0.0);
            bet.setAmountLose(bet.getAmount());
        } else {
            if (dataResults.getPrize() == 100.0 && ROUND.contains(dataResults.getKey())) {
                bet.setStatus(Bet.BetStatus.DRAW.name());
                bet.setAmountWin(0.0);
            } else {
                bet.setStatus(Bet.BetStatus.WIN.name());
                bet.setAmountWin(dataResults.getPrize() * bet.getAmount());
            }
        }
        bet.setUpdatedDate(new Date());
    }
}
