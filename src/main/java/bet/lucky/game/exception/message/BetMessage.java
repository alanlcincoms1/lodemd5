package bet.lucky.game.exception.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum BetMessage implements ApplicationMessage {
    INVALID_PARAMETER("LUCKY00002", "Đặt cược bị lỗi, vui lòng thử lại!", HttpStatus.BAD_REQUEST.value()),
    INVALID_LOGIN("LUCKY00003", "Vui lòng đăng nhập để quay!", HttpStatus.UNAUTHORIZED.value());

    private String message;
    private String description;
    private int statusCode;

    @Override
    public String getKey() {
        return this.name();
    }
}
