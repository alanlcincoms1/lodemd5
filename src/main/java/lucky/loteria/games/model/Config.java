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
    @SerializedName("prize")
    String prize;

    public List<Double> getListPrize() {
        List<Double> indexs = Arrays.stream(prize.split(",")).map(Double::parseDouble).collect(Collectors.toList());
        Collections.shuffle(indexs);
        return indexs;
    }
}
