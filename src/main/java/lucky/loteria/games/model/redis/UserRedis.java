package lucky.loteria.games.model.redis;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lucky.loteria.games.external_dto.response.UserTokenDto;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@RedisHash("UserRedis")
@Data
@NoArgsConstructor
public class UserRedis extends UserTokenDto implements Serializable {
    @Id
    private String token;

    @TimeToLive
    public long getTimeToLive() {
        return 60*30;
    }
}