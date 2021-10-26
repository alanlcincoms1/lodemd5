package bet.lucky.game.model;

import bet.lucky.game.constance.Constance;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@SqlResultSetMapping(
        name = Constance.BET_TOP_MAPPING,
        entities = @EntityResult(
                entityClass = BetTop.class,
                fields = {
                        @FieldResult(name = "fullname", column = "fullname"),
                        @FieldResult(name = "amount", column = "amount")

                }
        )
)
@NoArgsConstructor
public class BetTop implements Serializable {

    private static final long serialVersionUID = -2054389137452244283L;
    @Id
    String fullname;
    Double amount;
}
