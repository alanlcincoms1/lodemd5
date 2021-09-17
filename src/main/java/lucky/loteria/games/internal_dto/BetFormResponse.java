package lucky.loteria.games.internal_dto;

import lombok.Data;
import lucky.loteria.games.model.Bet;

@Data
public class BetFormResponse {
	private Bet bet;
	private String message;
	private Integer status;
}
