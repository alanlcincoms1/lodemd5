package bet.lucky.game.external_dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserBalanceResponseDto extends ExternalResponseDto implements Serializable {
    private static final long serialVersionUID = -7954378930788214433L;
    private List<UserBalanceDto> data;
}
