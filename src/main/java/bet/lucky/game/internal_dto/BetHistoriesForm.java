package bet.lucky.game.internal_dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class BetHistoriesForm {
    private String token;
    private List<String> status;
    private long tableId;
    private Integer roundId;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private Date date;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private Date toDate;

    private int limit = 5;
    private int page = 0;
}
