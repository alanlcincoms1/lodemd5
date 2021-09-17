package lucky.loteria.games.external_dto.request;

import lombok.Data;

@Data
public class BetDataRequest {
    public String game_id;
    public String game_name;
    public String game_round_id;
    public String game_ticket_id;
    public String game_ticket_status;
    public String game_your_bet;
    public Double game_stake;
    public Double game_winlost;
    public String current_result;
}
