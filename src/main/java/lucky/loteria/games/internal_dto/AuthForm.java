package lucky.loteria.games.internal_dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AuthForm {
	@NotBlank(message = "Token must not be blank!")
	private String token;
}

