package lucky.loteria.games.repository.impl;

import lucky.loteria.games.model.redis.UserRedis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRedisRepository extends CrudRepository<UserRedis, String>{
    public UserRedis findUserRedisByTokenEquals(String token);
}