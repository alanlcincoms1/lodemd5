package lucky.loteria.games.internal_dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class AuthForm {
	@NotBlank(message = "Token must not be blank!")
	private String token;
	@NotBlank(message = "Agent must not be blank!")
	private Integer agentId;
}

