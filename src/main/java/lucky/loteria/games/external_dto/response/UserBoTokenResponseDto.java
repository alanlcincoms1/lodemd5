package lucky.loteria.games.external_dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserBoTokenResponseDto extends ExternalResponseDto{
    private List<UserBo> data;
}
