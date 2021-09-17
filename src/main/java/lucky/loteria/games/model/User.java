package lucky.loteria.games.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String uid;
    private String memberId;
    private Integer agentId;
    private String fullName;
    private String username;
    private Double balance;
    private String status;

    private String agentCodeId;
    private String agentCode;
    private Date createdDate;
    private Date updatedDate;
}
