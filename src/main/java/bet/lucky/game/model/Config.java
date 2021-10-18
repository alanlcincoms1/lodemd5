package bet.lucky.game.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

@Data
public class Config implements Serializable {
    @SerializedName("reel")
    Integer reel;
    @SerializedName("start")
    Integer start;
    @SerializedName("distribution")
    Integer distribution;
    @SerializedName("prize")
    Double prize;

}
