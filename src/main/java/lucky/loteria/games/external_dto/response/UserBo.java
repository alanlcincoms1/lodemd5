package lucky.loteria.games.external_dto.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserBo implements Serializable {
    @SerializedName("partner_code")
    String partnerCode;
    String token;
    String ip;
    String os;
    String device;
    String browser;
    String expried;
    String fullname;
    String username;
    String level;
    String str;
    @SerializedName("last_login")
    String lastLogin;
    @SerializedName("login_count")
    int loginCount;
    String type;
    Boolean processed;
    String _id;
    Long id;
    @SerializedName("created_time")
    String createdTime;
    @SerializedName("last_updated_time")
    String lastUpdatedTime;
}
