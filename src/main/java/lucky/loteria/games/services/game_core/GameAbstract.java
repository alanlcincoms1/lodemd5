package lucky.loteria.games.services.game_core;

import javax.transaction.Transactional;


public abstract class GameAbstract implements IGame {
    public String groupName = "";

    public GameAbstract() {
    }

    @Transactional
    public void run() {
    }

}