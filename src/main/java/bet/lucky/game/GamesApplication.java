package bet.lucky.game;

import io.sentry.Sentry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@EnableRedisRepositories
@EnableTransactionManagement
public class GamesApplication {
    public static void main(String[] args) {
        try {
            SpringApplication springApplication = new SpringApplication(GamesApplication.class);
            springApplication.addListeners(new ApplicationPidFileWriter("lucky.pid"));
            springApplication.run(args);
        } catch (Exception e) {
            Sentry.capture(e);
            throw e;
        }
    }
}
