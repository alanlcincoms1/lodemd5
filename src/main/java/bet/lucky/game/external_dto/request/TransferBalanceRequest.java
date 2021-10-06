package bet.lucky.game.external_dto.request;

import lombok.Data;

@Data
public class TransferBalanceRequest {
    public String token;
    public String uid;
    public double amount;
    public String transaction_id;
    public String action;
    public BetDataRequest data;
}
