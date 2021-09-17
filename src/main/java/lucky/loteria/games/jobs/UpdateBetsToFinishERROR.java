package lucky.loteria.games.jobs;

import lucky.loteria.games.model.Bet;
import lucky.loteria.games.services.BetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by rajeevkumarsingh on 02/08/17.
 */
@Component
public class UpdateBetsToFinishERROR {

    @Autowired
    BetService betService;

    @Scheduled(fixedDelay = 10000)
    public void scheduleTaskWithFixedDelay() {
        betService.updateBetsError(new String[]{Bet.BetStatus.WIN.name(), Bet.BetStatus.LOSE.name()});
    }
}