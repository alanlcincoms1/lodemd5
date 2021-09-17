package lucky.loteria.games.services.game_core;

import lucky.loteria.games.constance.Constance;
import lucky.loteria.games.services.game_instance.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
     * Created by rajeevkumarsingh on 02/08/17.
     */
@Component
public class GameFactory {
    @Autowired
    GameLucky gameLucky;

    public GameAbstract getInstance(String type){
        if (Constance.GROUP_NAME_LUCKY.equals(type)) return gameLucky;
        return null;
    }
}