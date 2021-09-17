package lucky.loteria.games.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

@Data
public class SimpleConfig implements Serializable {
    @SerializedName("prize")
    String prize;
}
