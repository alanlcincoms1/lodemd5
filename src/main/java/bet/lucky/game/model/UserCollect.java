package bet.lucky.game.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity(name = "user_collects")
@EqualsAndHashCode(callSuper = false)
public class UserCollect extends BaseEntity {
    private long tableId;
    private String uid;

    private Double jackpot;
    private String fullname;

    @Column(precision = 20, scale = 3)
    private Double totalAmountWin;
}
