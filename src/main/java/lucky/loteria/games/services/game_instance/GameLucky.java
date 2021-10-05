package lucky.loteria.games.services.game_instance;

import lucky.loteria.games.constance.Constance;
import lucky.loteria.games.model.Bet;
import lucky.loteria.games.model.Config;
import lucky.loteria.games.model.Table;
import lucky.loteria.games.model.UserCollect;
import lucky.loteria.games.model.redis.ConfigurationRedis;
import lucky.loteria.games.repository.impl.UserCollectRepository;
import lucky.loteria.games.services.game_core.DataResults;
import lucky.loteria.games.services.game_core.GameAbstract;
import lucky.loteria.games.utils.GameUtils;
import lucky.loteria.games.utils.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

@Service
public class GameLucky extends GameAbstract {
    public static final String GROUP_NAME = Constance.GROUP_NAME_LUCKY;

    private final UserCollectRepository userCollectRepository;

    public GameLucky(
            UserCollectRepository userCollectRepository) {
        this.userCollectRepository = userCollectRepository;
        groupName = GameLucky.GROUP_NAME;
    }

    @Override
    public DataResults createRandomResult(Table table, Bet bet, ConfigurationRedis configurationRedis) {
        HashMap<String, Config> collection = configurationRedis.getCollection();
        int num = RandomUtils.randomNumber(configurationRedis.getStart(), configurationRedis.getTotalDistribution());
        String[] keys = configurationRedis.getEvent();
        String k = "";
        HashMap<String, Config> result = new HashMap<>();

        for (String key : keys) {
            Config config = collection.get(key);
            if (num > config.getStart() && num <= config.getDistribution()) {
                result.put(key, config);
                k = key;
                break;
            }
        }
        DataResults dataResult = new DataResults();
        dataResult.setResult(result.get(k));
        dataResult.setKey(k);
        return dataResult;
    }


    public void updateBetAfterResult(Bet bet, DataResults dataResults, String username, Double betAmount) {
        UserCollect userCollect = userCollectRepository.findByTableIdAndUid(bet.getTableId(), bet.getUid());
        if (userCollect == null) {
            userCollect = new UserCollect();
            userCollect.setTotalAmountWin(0.0);
            userCollect.setUsername(GameUtils.convertUS(username));
            userCollect.setTableId(bet.getTableId());
            userCollect.setUid(bet.getUid());
        }
        Config result = dataResults.getResult();

        if (result.isLose()) {
            bet.setStatus(Bet.BetStatus.LOSE.name());
            bet.setAmountLose(betAmount * result.getPrize());
            bet.setAmountWin(0.0);
        } else {
            bet.setStatus(Bet.BetStatus.WIN.name());
            bet.setAmountWin(betAmount * result.getPrize());
            bet.setAmountLose(0.0);
            userCollect.setTotalAmountWin(userCollect.getTotalAmountWin() + bet.getAmountWin() * Constance.DONGIA_VND);
        }
        bet.setPrize(result.getPrize());
        bet.setReel(result.getReel());
        userCollectRepository.save(userCollect);
        bet.setUpdatedDate(new Date());
    }
}
