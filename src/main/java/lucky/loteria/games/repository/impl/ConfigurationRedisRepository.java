package lucky.loteria.games.repository.impl;

import lucky.loteria.games.model.redis.ConfigurationRedis;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.CrudRepository;

@Configuration
public interface ConfigurationRedisRepository extends CrudRepository<ConfigurationRedis, Long> {
    ConfigurationRedis findByTableId(long tableId);
}
