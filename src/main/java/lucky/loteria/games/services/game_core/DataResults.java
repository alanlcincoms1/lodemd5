package lucky.loteria.games.services.game_core;

import lombok.Data;
import lucky.loteria.games.model.Bet;
import lucky.loteria.games.model.Config;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class DataResults implements Serializable {
    Config result;
    String key;
    Double prize;

    public boolean isLose() {
        return prize == 0;
    }
}

