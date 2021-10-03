package lucky.loteria.games.internal_dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class BetForm {
	@NotBlank(message = "Token must not be blank!")
	private String token;
	@NotBlank(message = "Table must not be blank!")
	private Integer tableId;
	@NotBlank(message = "betAmount must not be blank!")
	private Double betAmount;
}
