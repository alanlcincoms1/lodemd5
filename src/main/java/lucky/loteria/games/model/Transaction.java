package lucky.loteria.games.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "transaction")
@Data
public class Transaction {
	public static enum  TransactionStatus
	{
		NONE,
		SUCCESS,
		FAIL
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

//	private Integer agentId;
	private String memberId;
	private Double amount;
	private Double amountBefore;
	private Double amountAfter;
	private Double reqAmount;
	private Double duesAmount;
	@ManyToOne
	private Bet bet;
	private String agentTransactionId;
	private String transactionHash;
	private String status;
	private String type;
	private String note;

	private Date createdDate;

	private Date updatedDate;
}
