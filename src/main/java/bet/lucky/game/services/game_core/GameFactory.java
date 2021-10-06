package bet.lucky.game.services.game_core;

import bet.lucky.game.constance.Constance;
import bet.lucky.game.services.game_instance.GameLucky;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameFactory {
    @Autowired
    GameLucky gameLucky;

    public GameAbstract getInstance(String type) {
        if (Constance.GROUP_NAME_LUCKY.equals(type)) return gameLucky;
        return null;
    }
}