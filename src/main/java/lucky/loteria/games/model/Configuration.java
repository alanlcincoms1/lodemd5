package lucky.loteria.games.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lucky.loteria.games.model.redis.ConfigurationRedis;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.lang.reflect.Type;
import java.util.HashMap;


@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Configuration extends BaseEntity {
    Long tableId;
    Double prize;
    @Column(columnDefinition = "TEXT")
    String collection;
    Integer totalDistribution;
    String event;
    String alphabetEvent;
    Integer start;

    public HashMap<String, Config> getCollection() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Type objectHashMap = new TypeToken<HashMap<String, Config>>() {
        }.getType();
        return gson.fromJson(this.collection, objectHashMap);
    }

    public HashMap<String, SimpleConfig> getSimpleCollection() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Type objectHashMap = new TypeToken<HashMap<String, SimpleConfig>>() {
        }.getType();
        return gson.fromJson(this.collection, objectHashMap);
    }

    public ConfigurationRedis toRedisObject() {
        ConfigurationRedis c = new ConfigurationRedis();
        c.setTableId(this.tableId);
        c.setPrize(this.prize);
        c.setCollection(this.collection);
        c.setTotalDistribution(this.totalDistribution);
        c.setEvent(this.event);
        c.setAlphabetEvent(this.alphabetEvent);
        c.setStart(this.start);
        return c;
    }

    public String[] getEvent() {
        return this.event.split(",");
    }
}
