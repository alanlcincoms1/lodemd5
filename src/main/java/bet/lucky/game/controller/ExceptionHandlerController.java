package bet.lucky.game.controller;

import bet.lucky.game.exception.ApplicationException;
import bet.lucky.game.external_dto.response.ErrorResponse;
import bet.lucky.game.utils.ResponseFactory;
import bet.lucky.game.utils.Utilities;
import io.sentry.Sentry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(value = ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException exception, HttpServletRequest httpServletRequest) {
        Utilities.LOGGER.error("Application error", exception);
        Sentry.getContext().addTag("os", httpServletRequest.getHeader("User-Agent"));
        Sentry.getContext().addExtra("remote_user", httpServletRequest.getRemoteUser());
        Sentry.getContext().addExtra("ip", httpServletRequest.getRemoteAddr());
        Sentry.getContext().addExtra("os", httpServletRequest.getHeader("User-Agent"));
        Sentry.capture(exception);
        return ResponseFactory.error(exception.getError());
    }

}
