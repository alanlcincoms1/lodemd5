package bet.lucky.game.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;


@Entity(name = "session_log")
@Data
public class SessionLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String memberId;
	private String uid;
	private String ip;
	private String token;
	private String userAgent;
	private Date createdDate;
	private Date updatedDate;
}
