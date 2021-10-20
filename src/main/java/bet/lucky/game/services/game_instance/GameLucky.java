package bet.lucky.game.services.game_instance;

import bet.lucky.game.constance.Constance;
import bet.lucky.game.model.Bet;
import bet.lucky.game.model.Config;
import bet.lucky.game.model.Jackpot;
import bet.lucky.game.model.Tables;
import bet.lucky.game.model.redis.ConfigurationRedis;
import bet.lucky.game.repository.impl.JackpotRepository;
import bet.lucky.game.services.game_core.DataResults;
import bet.lucky.game.services.game_core.GameAbstract;
import bet.lucky.game.utils.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class GameLucky extends GameAbstract {
    public static final String GROUP_NAME = Constance.GROUP_NAME_LUCKY;

    private final JackpotRepository jackpotRepository;

    public GameLucky(JackpotRepository jackpotRepository) {
        this.jackpotRepository = jackpotRepository;
        groupName = GameLucky.GROUP_NAME;
    }

    @Override
    public DataResults createRandomResult(Tables tables, Bet bet, ConfigurationRedis configurationRedis) {
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


    public void updateBetAfterResult(Tables tables, Bet bet, DataResults dataResults, String fullname, Double betAmount) {
        List<Jackpot> lstJackpot = jackpotRepository.findAll();
        Jackpot jackpot = lstJackpot.get(0);
        Config result = dataResults.getResult();
        double amount;
        double jackPotAmount = getPercentBetAmount(betAmount, jackpot.getJackpotPercent());
        if (result.getPrize() == 100) {
            amount = (jackpot.getJackpot() + jackPotAmount) - betAmount;
            jackpot.setJackpot(jackpot.getInitJackpotAmount());
        } else {
            amount = (betAmount * result.getPrize()) - betAmount;
            jackpot.setJackpot(jackpot.getJackpot() + jackPotAmount);
        }
        if (amount > 0) {
            bet.setStatus(Bet.BetStatus.WIN.name());
            bet.setAmountWin(amount);
            bet.setAmountLose(0.0);
        } else {
            bet.setStatus(Bet.BetStatus.LOSE.name());
            bet.setAmountLose(Math.abs(amount));
            bet.setAmountWin(0.0);
        }
        jackpotRepository.save(jackpot);
        bet.setPrize(result.getPrize());
        bet.setReel(result.getReel());
        bet.setUpdatedDate(new Date());
    }

    private static double getPercentBetAmount(double betAmount, double percent) {
        return (betAmount / 100) * percent;
    }
}
