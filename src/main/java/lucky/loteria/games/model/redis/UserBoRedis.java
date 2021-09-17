package lucky.loteria.games.model.redis;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lucky.loteria.games.external_dto.response.UserBo;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@RedisHash("UserBoRedis")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class UserBoRedis extends UserBo {
    @Id
    private String token;

    @TimeToLive
    public Long getExpiredTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            Date date = sdf.parse(this.getExpried());
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }
}
