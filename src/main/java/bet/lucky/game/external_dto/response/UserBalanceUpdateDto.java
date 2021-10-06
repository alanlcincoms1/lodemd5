package bet.lucky.game.external_dto.response;

import lombok.Data;

@Data
public class UserBalanceUpdateDto {
    private Double amount;
    private Double amount_before;
    private Double amount_after;
    private Double req_amount;
    private Double dues_amount;
    private String transaction_id;
    private String agency_transaction_id;
    private String status;
    private String message;
    private Integer error_code;
}
