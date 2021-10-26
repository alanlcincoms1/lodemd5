package bet.lucky.game.external_dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BetResponseDto {
    private Long id;
    private Date createdDate;
    private Date updatedDate;
    private String ip;
    private String uid;
    private String memberId;
    private long tableId;
    private Double amount;
    private Double amountWin;
    private int reel;
    private Double prize;
    private String status;
    private Integer isRunning;
    private String fullname;
    private String transactionHash;
}
