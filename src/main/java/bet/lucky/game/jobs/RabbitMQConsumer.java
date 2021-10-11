package bet.lucky.game.jobs;

import bet.lucky.game.external_dto.response.BalanceRabitmqResponse;
import bet.lucky.game.repository.impl.UserRepository;
import bet.lucky.game.services.SocketService;
import com.google.gson.Gson;
import io.sentry.Sentry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class RabbitMQConsumer {
    @Autowired
    UserRepository userRepository;

    @Autowired
    SocketService socketService;

    @Autowired
    Gson gson;

    public Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    //	@RabbitListener(queues = "webminigame_balancer")
    public void receivedMessage(String message) {
        try {
            BalanceRabitmqResponse balanceRabitmqResponse = (BalanceRabitmqResponse) gson.fromJson(message, BalanceRabitmqResponse.class);
//			User user = userRepository.getUserByUidEquals(balanceRabitmqResponse.getUid());
//			if (user != null && user.getId() > 0) {
//				user.setBalance(balanceRabitmqResponse.getMain_balance());
//				user.setUpdatedDate(new Date());
//				userRepository.save(user);
//				Map<String, Object> data = new HashMap<>();
//				data.put("main_balance", balanceRabitmqResponse.getMain_balance());
//
//
//			}
            Map<String, Object> data = new HashMap<>();
            data.put("main_balance", balanceRabitmqResponse.getMain_balance());
            if (!balanceRabitmqResponse.getUid().contains("botslot") && !balanceRabitmqResponse.getUid().contains("go88_")) {
                Thread a = new Thread(() -> socketService.sendToUser(balanceRabitmqResponse.getUid(), "balance", data));
                a.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Sentry.capture(e);
        }

    }
}