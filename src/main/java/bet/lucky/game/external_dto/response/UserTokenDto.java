package bet.lucky.game.external_dto.response;

import lombok.Data;

import java.util.Date;


@Data
public class UserTokenDto {
    private Double main_balance;
    private Double extra_balance;
    private long time;
    private String fullname;
    private String avatar;
    private String group;
    private String aff_id;
    private String g_id;
    private String uid;
    private String member_id;
    private Date last_login;
    private Date expried;
    private String status;
    private String token;
    private Integer agency_id;
    private String agency_code;

}
