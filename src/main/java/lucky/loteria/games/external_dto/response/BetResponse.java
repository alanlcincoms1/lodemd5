package lucky.loteria.games.external_dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BetResponse {
    Integer reel;
    Double prize;
    String username;
}
