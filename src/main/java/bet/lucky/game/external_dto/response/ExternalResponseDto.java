package bet.lucky.game.external_dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class ExternalResponseDto implements Serializable {
    private static final long serialVersionUID = 2394623489033991211L;
    public String status;
    public String code;
    public String message;
}
