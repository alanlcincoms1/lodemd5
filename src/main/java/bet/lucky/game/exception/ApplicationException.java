package bet.lucky.game.exception;

import bet.lucky.game.exception.message.ApplicationMessage;
import bet.lucky.game.exception.message.InternalServerMessage;

public class ApplicationException extends RuntimeException {

    private static final long serialVersionUID = 8133784150850998787L;

    public ApplicationMessage getError() {
        return error;
    }

    private final transient ApplicationMessage error;

    public ApplicationException() {
        this.error = InternalServerMessage.INTERNAL_SERVER_ERROR;
    }

    public ApplicationException(String message) {
        super(message);
        this.error = InternalServerMessage.INTERNAL_SERVER_ERROR;
    }

    public ApplicationException(
            ApplicationMessage error) {
        super(error.getDescription());
        this.error = error;
    }

    public ApplicationException(Throwable cause, ApplicationMessage error) {
        super(error.getDescription(), cause);
        this.error = error;
    }

    public ApplicationException(String message, ApplicationMessage error) {
        super(message);
        this.error = error;
    }

}
