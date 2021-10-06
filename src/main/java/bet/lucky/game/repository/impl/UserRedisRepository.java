package bet.lucky.game.repository.impl;

import bet.lucky.game.model.redis.UserRedis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRedisRepository extends CrudRepository<UserRedis, String>{
    public UserRedis findUserRedisByTokenEquals(String token);
}