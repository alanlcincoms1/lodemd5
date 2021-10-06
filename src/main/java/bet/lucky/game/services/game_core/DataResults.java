package bet.lucky.game.services.game_core;

import bet.lucky.game.model.Config;
import lombok.Data;

import java.io.Serializable;

@Data
public class DataResults implements Serializable {
    Config result;
    String key;
}

