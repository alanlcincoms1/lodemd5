package bet.lucky.game.external_dto.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class BetTopResponse implements Serializable {
    private static final long serialVersionUID = 3046808803417934016L;
    String fullname;
    Double amount;
}
