package lucky.loteria.games.model.redis;

import com.google.gson.Gson;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lucky.loteria.games.model.Config;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode
@RedisHash(value = "Configuration", timeToLive = 24 * 60 * 60)
public class ConfigurationRedis {
    @Id
    Long tableId;
    Double prize;
    String collection;
    Integer totalDistribution;
    String event;
    String alphabetEvent;
    Integer start;

    public HashMap<String, Config> getCollection() {
        Map<String, Config> map = new HashMap<>();
        map = new Gson().fromJson(this.collection, map.getClass());
        String[] keys = this.getEvent();
        HashMap<String, Config> result = new HashMap<>();
        for (String key : keys) {
            String json = new Gson().toJson(map.get(key));
            Config c = new Gson().fromJson(json, Config.class);
            result.put(key, c);
        }
        return result;
    }

    public String[] getEvent() {
        return this.event.split(",");
    }
}
