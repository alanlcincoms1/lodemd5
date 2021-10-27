package bet.lucky.game.controller;

import bet.lucky.game.exception.ApplicationException;
import bet.lucky.game.external_dto.response.ErrorResponse;
import bet.lucky.game.utils.ResponseFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {

    @ExceptionHandler(value = ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException exception, HttpServletRequest httpServletRequest) {
        log.error("Application error: {}", exception);
        log.info("os", httpServletRequest.getHeader("User-Agent"));
        log.info("remote_user", httpServletRequest.getRemoteUser());
        log.info("ip", httpServletRequest.getRemoteAddr());
        log.info("os", httpServletRequest.getHeader("User-Agent"));
        return ResponseFactory.error(exception.getError());
    }

}
