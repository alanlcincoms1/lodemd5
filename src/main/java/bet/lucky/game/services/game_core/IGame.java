package bet.lucky.game.services.game_core;

import bet.lucky.game.model.Bet;
import bet.lucky.game.model.Table;
import bet.lucky.game.model.redis.ConfigurationRedis;
import org.springframework.stereotype.Service;

@Service
public interface IGame {
    DataResults createRandomResult(Table table, Bet bet, ConfigurationRedis configurationRedis);

    void updateBetAfterResult(Bet bet, DataResults dataResults, String username, Double betAmount);
}