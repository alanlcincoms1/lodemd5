package bet.lucky.game.external_dto.request;

import lombok.Data;

@Data
public class Transfer {
    public Long member_id;
    public String uid;
    public double amount;
    public String transaction_id;
    public String action;
    public String option;
    public BetDataRequest data;
}
