package lucky.loteria.games.services.game_core;

import lucky.loteria.games.model.Bet;
import lucky.loteria.games.model.Table;
import lucky.loteria.games.model.redis.ConfigurationRedis;
import org.springframework.stereotype.Service;

@Service
public interface IGame {
    DataResults createRandomResult(Table table, Bet bet, ConfigurationRedis configurationRedis);

    void updateBetAfterResult(Bet bet, DataResults dataResults, String username, Double betAmount);
}