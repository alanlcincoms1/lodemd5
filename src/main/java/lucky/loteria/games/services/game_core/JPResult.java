package lucky.loteria.games.services.game_core;

import lombok.Data;

import java.io.Serializable;

@Data
public class JPResult implements Serializable {
    Integer times = 1;
    String[] resultRaw = null;
}
