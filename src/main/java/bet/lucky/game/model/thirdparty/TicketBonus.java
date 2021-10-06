package bet.lucky.game.model.thirdparty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "TicketBonus", timeToLive = 24 * 60 * 60)
public class TicketBonus {
    @Id
    private long userId;
    private String username;
    private double balanceBet;
    private double remainingBalance;
    private int ticket;
    private int ticketUsed;
    private int freeSpin = 0;
}
