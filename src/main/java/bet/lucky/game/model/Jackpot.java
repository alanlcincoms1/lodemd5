package bet.lucky.game.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "jackpot")
@Data
public class Jackpot {
    private Double jackpot;
    private Double jackpotPercent;
    @Id
    private Double initJackpotAmount;
}
