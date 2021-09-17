package lucky.loteria.games.repository.impl;

import lucky.loteria.games.model.Configuration;
import lucky.loteria.games.model.redis.ConfigurationRedis;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigurationRepository extends CrudRepository<Configuration, String> {
    Configuration findByTableId(long tableId);

    @Query(value = "select * from configuration", nativeQuery = true)
    List<Configuration> getAll();
}
