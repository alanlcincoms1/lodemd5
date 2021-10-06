package bet.lucky.game.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
public class BetStatement {
	private Date betDate;
	private String betStatus;
	private Double amountTotal;
	private Double amountWinTotal;
}

