package lucky.loteria.games.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    public boolean isLose() {
        return prize == 0;
    }

}
