package lucky.loteria.games.external_dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Builder
public class UserCollected implements Serializable {
    private Long id;
    private String username;
    private String alphabet;
    private double prize;
    private Boolean isCollected;
    private Double totalWin;
}
