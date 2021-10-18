package bet.lucky.game.external_dto.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class BetResponse implements Serializable {
    private static final long serialVersionUID = -8986867204676972487L;
    Integer reel;
    Double prize;
    String username;
}
