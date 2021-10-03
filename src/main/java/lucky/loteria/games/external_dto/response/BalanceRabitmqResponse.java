package lucky.loteria.games.external_dto.response;

import lombok.Data;

@Data
public class BalanceRabitmqResponse {
    private String uid;
    private double main_balance;
    private String time;
}
