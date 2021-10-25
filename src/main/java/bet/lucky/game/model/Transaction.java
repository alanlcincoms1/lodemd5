package bet.lucky.game.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "transaction")
@Data
public class Transaction {
    public static enum TransactionStatus {
        NONE,
        SUCCESS,
        FAIL
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Long memberId;
    private Double amount;
    private Double amountBefore;
    private Double amountAfter;
    private Double reqAmount;
    private Double duesAmount;
    @ManyToOne
    private Bet bet;
    private String agentTransactionId;
    private String status;
    private String type;
    private String note;

    private Date createdDate;

    private Date updatedDate;
}
