package lucky.loteria.games.controller;

import io.sentry.Sentry;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

public abstract class ExceptionHandle {

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Đặt cược bị lỗi, vui lòng thử lại!")
    @ExceptionHandler(Exception.class)
    public void conflict(Exception ex, HttpServletRequest httpServletRequest) {
        Sentry.getContext().addTag("os", httpServletRequest.getHeader("User-Agent"));
        Sentry.getContext().addExtra("remote_user", httpServletRequest.getRemoteUser());
        Sentry.getContext().addExtra("ip", httpServletRequest.getRemoteAddr());
        Sentry.getContext().addExtra("os", httpServletRequest.getHeader("User-Agent"));
        Sentry.capture(ex);
        ex.printStackTrace();
    }
}
