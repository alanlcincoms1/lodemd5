package lucky.loteria.games.external_dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LuckyTopResponse {
    Long id;
    String username;
    Double totalAmountWin;
}
