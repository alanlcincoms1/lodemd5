package bet.lucky.game.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "jackpot")
@Data
public class Jackpot {
    private BigDecimal jackpot;
    private BigDecimal jackpotPercent;
    @Id
    private BigDecimal initJackpotAmount;
}
