package bet.lucky.game.external_dto.request;

import lombok.Data;

import java.util.List;

@Data
public class TransferBalanceRequest {
    public Integer agency_id;
    List<Transfer> transfers;
}
