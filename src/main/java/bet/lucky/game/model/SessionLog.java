package bet.lucky.game.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "session_log")
@Data
public class SessionLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private Long memberId;
	private String uid;
	private String ip;
	private String token;
	private String userAgent;
	private Date createdDate;
	private Date updatedDate;
}
