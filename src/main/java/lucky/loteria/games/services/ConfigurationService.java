package lucky.loteria.games.services;

import lucky.loteria.games.model.Configuration;
import lucky.loteria.games.model.redis.ConfigurationRedis;
import lucky.loteria.games.repository.impl.ConfigurationRedisRepository;
import lucky.loteria.games.repository.impl.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ConfigurationService {
    @Autowired
    ConfigurationRepository configurationRepository;

    @Autowired
    ConfigurationRedisRepository configurationRedisRepository;

    public ConfigurationRedis getConfig(long tableId) {
        Optional<ConfigurationRedis> configuration = configurationRedisRepository.findById(tableId);
        if(configuration.isEmpty()) {
            Configuration c = configurationRepository.findByTableId(tableId);
            if(c != null) {
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
