package bet.lucky.game.repository.impl;

import bet.lucky.game.model.Configuration;
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
