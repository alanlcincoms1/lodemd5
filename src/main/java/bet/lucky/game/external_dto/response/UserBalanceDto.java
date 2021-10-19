package bet.lucky.game.external_dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserBalanceDto implements Serializable {
    private static final long serialVersionUID = 2462886592281255187L;
    private String uid;
    private Long member_id;
    private Double extra_balance;
    private Double main_balance;
    private Long time;
}
