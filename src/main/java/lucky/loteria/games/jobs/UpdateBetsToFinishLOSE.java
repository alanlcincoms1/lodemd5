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
public class UpdateBetsToFinishLOSE {

    @Autowired
    BetService betService;

    @Scheduled(fixedDelay = 2000)
    public void scheduleTaskWithFixedDelay() {
        betService.updateBetsAfterResult(new String[]{Bet.BetStatus.LOSE.name()});
    }
}