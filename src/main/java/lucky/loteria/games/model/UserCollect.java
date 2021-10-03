package lucky.loteria.games.model;

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

    private String collection;
    private String username;

    @Column(precision = 20, scale = 3)
    private Double totalAmountWin;
}
