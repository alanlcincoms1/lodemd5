package lucky.loteria.games.services.game_core;

import lombok.Data;
import lucky.loteria.games.model.Config;

import java.io.Serializable;

@Data
public class DataResults implements Serializable {
    Config result;
    String key;
}

