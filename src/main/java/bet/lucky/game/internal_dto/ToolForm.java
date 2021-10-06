package bet.lucky.game.internal_dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class ToolForm {
	private String result;
	private Integer tableId;
	private String pass;
}
