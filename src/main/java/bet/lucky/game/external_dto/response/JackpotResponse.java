package bet.lucky.game.external_dto.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
public class JackpotResponse implements Serializable {
    private static final long serialVersionUID = 7753109864935849655L;
    BigDecimal jackpot;
}
