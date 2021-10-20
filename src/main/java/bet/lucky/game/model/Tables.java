package bet.lucky.game.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tables")
@Data
@EqualsAndHashCode(callSuper = false)
public class Tables extends BaseEntity {

    public enum TableStatus {
        ACTIVE(1),
        INACTIVE(0);
        private Integer value;

        TableStatus(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return this.value;
        }
    }

    private String name;
    private String cheat;
    private String groupName;
    private Integer status;
    private String gameId;
}
