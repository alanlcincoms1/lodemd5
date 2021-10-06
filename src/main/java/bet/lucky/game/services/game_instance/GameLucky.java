package bet.lucky.game.services.game_instance;

import bet.lucky.game.constance.Constance;
import bet.lucky.game.model.Bet;
import bet.lucky.game.model.Config;
import bet.lucky.game.model.Table;
import bet.lucky.game.model.redis.ConfigurationRedis;
import bet.lucky.game.model.UserCollect;
import bet.lucky.game.repository.impl.UserCollectRepository;
import bet.lucky.game.services.game_core.DataResults;
import bet.lucky.game.services.game_core.GameAbstract;
import bet.lucky.game.utils.GameUtils;
import bet.lucky.game.utils.RandomUtils;
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
