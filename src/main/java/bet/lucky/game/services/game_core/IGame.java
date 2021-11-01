package bet.lucky.game.services.game_core;

import bet.lucky.game.exception.WalletException;
import bet.lucky.game.external_dto.response.BetResponse;
import bet.lucky.game.model.Bet;
import bet.lucky.game.model.Tables;
import bet.lucky.game.model.redis.ConfigurationRedis;
import org.springframework.stereotype.Service;

@Service
public interface IGame {
    DataResults createRandomResult(Tables tables, Bet bet, ConfigurationRedis configurationRedis);

    BetResponse updateBetAfterResult(Tables tables, Bet bet, DataResults dataResults, String username, Double betAmount) throws WalletException;
}