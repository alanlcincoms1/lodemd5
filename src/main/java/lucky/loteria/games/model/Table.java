package lucky.loteria.games.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lucky.loteria.games.constance.Constance;

import javax.persistence.*;
import java.sql.Date;

@Entity(name = "tables")
@Data
@EqualsAndHashCode(callSuper = false)
public class Table extends BaseEntity {

	public enum TableStatus {
		ACTIVE(1),
		INACTIVE(0);
		private Integer value;
		TableStatus(Integer value) {
			this.value = value;
		}
		public Integer getValue() { return this.value; }
	}

	private String name;
	private String cheat;
	private String groupName;
	private String type;
	private Integer indexOrder;
	private Double initJackpotAmount;
	private Integer status;

	public String toString(){
		return "";
	}
}
