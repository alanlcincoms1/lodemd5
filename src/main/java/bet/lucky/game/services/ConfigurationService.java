package bet.lucky.game.services;

import bet.lucky.game.model.Configuration;
import bet.lucky.game.model.redis.ConfigurationRedis;
import bet.lucky.game.repository.impl.ConfigurationRedisRepository;
import bet.lucky.game.repository.impl.ConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConfigurationService {
    private final ConfigurationRepository configurationRepository;

    private final ConfigurationRedisRepository configurationRedisRepository;

    public ConfigurationRedis getConfig(long tableId) {
        Optional<ConfigurationRedis> configuration = configurationRedisRepository.findById(tableId);
        if (configuration.isEmpty()) {
            Configuration c = configurationRepository.findByTableId(tableId);
            if (c != null) {
                configurationRedisRepository.save(c.toRedisObject());
                return c.toRedisObject();
            } else {
                return null;
            }
        }
        return configuration.get();
    }

    public List<Configuration> getAll() {
        return configurationRepository.getAll();
    }
}
