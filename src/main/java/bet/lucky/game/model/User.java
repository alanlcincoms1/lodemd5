package bet.lucky.game.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String uid;
    private Long memberId;
    private Integer agentId;
    private String fullName;
    private Double balance;
    private String status;
    private Date createdDate;
    private Date updatedDate;
}
