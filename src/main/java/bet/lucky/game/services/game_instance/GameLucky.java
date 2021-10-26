package bet.lucky.game.services.game_instance;

import bet.lucky.game.constance.Constance;
import bet.lucky.game.external_dto.response.UserBalanceUpdateDto;
import bet.lucky.game.model.Bet;
import bet.lucky.game.model.Config;
import bet.lucky.game.model.Jackpot;
import bet.lucky.game.model.Tables;
import bet.lucky.game.model.redis.ConfigurationRedis;
import bet.lucky.game.repository.impl.JackpotRepository;
import bet.lucky.game.services.UserService;
import bet.lucky.game.services.game_core.DataResults;
import bet.lucky.game.services.game_core.GameAbstract;
import bet.lucky.game.utils.RandomUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class GameLucky extends GameAbstract {
    public static final String GROUP_NAME = Constance.GROUP_NAME_LUCKY;

    private final UserService userService;
    private final JackpotRepository jackpotRepository;

    public GameLucky(UserService userService, JackpotRepository jackpotRepository) {
        this.userService = userService;
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
        BigDecimal amount;
        BigDecimal jackPotAmount = getPercentBetAmount(betAmount, jackpot.getJackpotPercent());
        if (result.getPrize() == 100) {
            amount = jackpot.getJackpot().add(jackPotAmount);
            jackpot.setJackpot(jackpot.getInitJackpotAmount());
        } else {
            amount = new BigDecimal(betAmount).multiply(new BigDecimal(result.getPrize()));
            jackpot.setJackpot(jackpot.getJackpot().add(jackPotAmount));
        }

        UserBalanceUpdateDto userBalanceUpdateDto =  userService.updateBalanceAfterBetResult(bet);

        bet.setStatus(Bet.BetStatus.WIN.name());
        bet.setAmountWin(amount.doubleValue());
        jackpotRepository.save(jackpot);
        bet.setPrize(result.getPrize());
        bet.setReel(result.getReel());
        bet.setUpdatedDate(new Date());
    }

    private static BigDecimal getPercentBetAmount(double betAmount, BigDecimal percent) {
        return new BigDecimal(betAmount).divide(new BigDecimal(100)).multiply(percent);
    }
}
