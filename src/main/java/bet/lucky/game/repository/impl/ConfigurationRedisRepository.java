package bet.lucky.game.repository.impl;

import bet.lucky.game.model.redis.ConfigurationRedis;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.CrudRepository;

@Configuration
public interface ConfigurationRedisRepository extends CrudRepository<ConfigurationRedis, Long> {
    ConfigurationRedis findByTableId(long tableId);
}
