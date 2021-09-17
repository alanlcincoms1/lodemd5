package lucky.loteria.games.internal_dto;

import lombok.Data;
import lucky.loteria.games.model.Bet;

@Data
public class BetJackpotResponse {
	private String fullName;
	private Double amount;
}
