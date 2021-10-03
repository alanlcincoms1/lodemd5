package lucky.loteria.games.services.game_core;

import javax.transaction.Transactional;

/**
 * Created by rajeevkumarsingh on 02/08/17.
 */

public abstract class GameAbstract implements IGame {
    public String groupName = "";

    public GameAbstract() {
    }

    @Transactional
    public void run() {
    }

}