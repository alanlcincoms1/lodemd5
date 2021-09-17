package lucky.loteria.games.services.game_core;

import com.google.gson.Gson;
import lucky.loteria.games.internal_dto.OddResponse;
import lucky.loteria.games.repository.impl.BetRepository;
import lucky.loteria.games.repository.impl.TableRepository;
import lucky.loteria.games.services.SocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by rajeevkumarsingh on 02/08/17.
 */

public abstract class GameAbstract implements IGame {
    private TableRepository tableRepository;
    private BetRepository betRepository;
    private SocketService socketService;
    private Gson gson;
    public Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    public String groupName = "";
    public static final String GAME_BET = "game-bet";
    public static final String GAME_SLOT = "game-slot";

    public GameAbstract(
            TableRepository tableRepository,
            BetRepository betRepository,
            SocketService socketService,
            Gson gson
    ) {
        this.tableRepository = tableRepository;
        this.betRepository = betRepository;
        this.socketService = socketService;
        this.gson = gson;
    }

    @Transactional
    public void run() {
    }

    public boolean isValidUid(String uid, double amount) {
        return true;
    }

    public List<OddResponse> getOddsByRound(){
        return null;
    }
}