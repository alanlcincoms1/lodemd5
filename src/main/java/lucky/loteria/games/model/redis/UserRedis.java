package lucky.loteria.games.model.redis;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lucky.loteria.games.external_dto.response.UserTokenDto;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@RedisHash(value = "UserRedis", timeToLive = 24 * 60 * 60)
@Data
@NoArgsConstructor
public class UserRedis extends UserTokenDto implements Serializable {
    @Id
    private String token;

}