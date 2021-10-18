package bet.lucky.game.external_dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class BetResponseDto {
    private Long id;
    private Date createdDate;
    private Date updatedDate;
    public enum BetStatus {
        ERROR(-1),
        NONE(0),
        BET(1),
        WIN(2),
        LOSE(3),
        DRAW(4);
        private final int value;

        BetStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum RUNNING_STATUS {
        NONE(0),
        RUNNING(1),
        FINISH(2),
        FINISH_JACKPOT(3),
        ERROR(4);
        int value;

        RUNNING_STATUS(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private String ip;
    private String uid;
    private String memberId;
    private long tableId;
    private Double amount;
    private Double amountLose;
    private Double amountWin;
    private int reel;
    private Double prize;
    private String status;
    private Integer isRunning;
    private String fullname;

    public Double getAmountWin() {
        return amountWin == null ? 0.0 : amountWin;
    }

    public Double getAmount() {
        return amount == null ? 0.0 : amount;
    }
}
