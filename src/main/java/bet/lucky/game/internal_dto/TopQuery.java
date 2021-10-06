package bet.lucky.game.internal_dto;

import lombok.Data;

@Data
public class TopQuery {
    private long tableId;
    private int limit = 5;
    private int page = 0;

    public long getTableId() {
        return tableId > 0 ? tableId : 1;
    }
}
