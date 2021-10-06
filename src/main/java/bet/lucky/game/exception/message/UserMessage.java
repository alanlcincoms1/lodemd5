package bet.lucky.game.exception.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserMessage implements ApplicationMessage {
    UNAUTHORIZED("LUCKY00001", "Unauthorized", HttpStatus.UNAUTHORIZED.value());

    private String message;
    private String description;
    private int statusCode;

    @Override
    public String getKey() {
        return this.name();
    }

}
