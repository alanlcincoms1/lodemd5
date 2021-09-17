package lucky.loteria.games.internal_dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OddResponse {
	private String name;
	private double rate;

}
