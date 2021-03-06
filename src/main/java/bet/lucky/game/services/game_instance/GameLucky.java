package bet.lucky.game.services.game_instance;

import bet.lucky.game.constance.Constance;
import bet.lucky.game.external_dto.response.BetResponse;
import bet.lucky.game.external_dto.response.UserBalanceUpdateDto;
import bet.lucky.game.model.Bet;
import bet.lucky.game.model.Config;
import bet.lucky.game.model.Jackpot;
import bet.lucky.game.model.Tables;
import bet.lucky.game.model.redis.ConfigurationRedis;
import bet.lucky.game.repository.impl.JackpotRepository;
import bet.lucky.game.services.BetService;
import bet.lucky.game.services.UserService;
import bet.lucky.game.services.game_core.DataResults;
import bet.lucky.game.services.game_core.GameAbstract;
import bet.lucky.game.utils.GameUtils;
import bet.lucky.game.utils.RandomUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class GameLucky extends GameAbstract {
    public static final String GROUP_NAME = Constance.GROUP_NAME_LUCKY;
    private final BetService betService;
    private final UserService userService;
    private final JackpotRepository jackpotRepository;

    public GameLucky(BetService betService, UserService userService, JackpotRepository jackpotRepository) {
        this.betService = betService;
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

    public BetResponse updateBetAfterResult(Tables tables, Bet bet, DataResults dataResults, String fullname, Double betAmount) {
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
        UserBalanceUpdateDto userBalanceUpdateDto = betService.transactionBet(bet, GameUtils.getUUID());
        if (amount.compareTo(new BigDecimal(0)) == 0) {
            bet.setStatus(Bet.BetStatus.LOSE.name());
        } else {
            bet.setStatus(Bet.BetStatus.WIN.name());
        }

        bet.setAmountWin(amount.doubleValue());
        jackpotRepository.save(jackpot);
        bet.setPrize(result.getPrize());
        bet.setReel(result.getReel());
        bet.setUpdatedDate(new Date());
        String transactionId = UUID.randomUUID().toString();

        userBalanceUpdateDto = betService.transactionBet(bet, transactionId);
        BetResponse betResponse = BetResponse.builder()
                .transaction_id(transactionId)
                .reel(dataResults.getResult().getReel())
                .prize(dataResults.getResult().getPrize())
                .balance(userBalanceUpdateDto.getAmount_after())
                .build();
        return betResponse;
    }

    private static BigDecimal getPercentBetAmount(double betAmount, BigDecimal percent) {
        return new BigDecimal(betAmount).divide(new BigDecimal(100)).multiply(percent);
    }
}
