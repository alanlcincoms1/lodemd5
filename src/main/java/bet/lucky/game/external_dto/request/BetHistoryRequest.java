package bet.lucky.game.external_dto.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class BetHistoryRequest implements Serializable {
    private String fullname;
    private Integer page;
    private Integer size;
}
