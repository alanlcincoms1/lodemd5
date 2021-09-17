package lucky.loteria.games.external_dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BetResponse {

    String round;
    Double prize;
    String username;
}
