package lucky.loteria.games.services.game_core;

import lucky.loteria.games.internal_dto.OddResponse;
import lucky.loteria.games.model.Bet;
import lucky.loteria.games.model.Table;
import lucky.loteria.games.model.redis.ConfigurationRedis;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by rajeevkumarsingh on 02/08/17.
 */
@Service
public interface IGame {
    public void run();
    public abstract DataResults createRandomResult(Table table, Bet bet, ConfigurationRedis configurationRedis, String round);
    public abstract boolean isValidBetType(String betTypeString);
    public abstract void updateBetAfterResult(Bet bet, DataResults dataResults, String username);
    public boolean isValidUid(String uid, double amount);
    public List<OddResponse> getOddsByRound();
}