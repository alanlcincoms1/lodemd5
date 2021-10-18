package bet.lucky.game.repository.impl;

import bet.lucky.game.model.BetTop;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomBetRepository {
    List<BetTop> getTopBet();
}
