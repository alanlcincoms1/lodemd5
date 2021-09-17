package lucky.loteria.games.model;

import java.util.Date;

public interface IBetStatement {
	Date getBetDate();
	Double getAmountTotal();
	Double getAmountWinTotal();
}
