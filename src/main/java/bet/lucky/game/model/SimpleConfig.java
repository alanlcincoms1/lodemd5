package bet.lucky.game.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

@Data
public class SimpleConfig implements Serializable {
    @SerializedName("total")
    Integer total;
    @SerializedName("prize")
    Double prize;
    @SerializedName("index")
    String index;
    @SerializedName("numberOfAlphabet")
    Integer numberOfAlphabet;
}
