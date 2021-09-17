package lucky.loteria.games.repository.impl;

import lucky.loteria.games.model.redis.UserBoRedis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBoRedisRepository extends CrudRepository<UserBoRedis, String> {

}
