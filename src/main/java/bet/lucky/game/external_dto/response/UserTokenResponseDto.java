package bet.lucky.game.external_dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
public class UserTokenResponseDto extends ExternalResponseDto {
	private List<UserTokenDto> data;
}
