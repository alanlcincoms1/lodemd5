package bet.lucky.game.controller;

import bet.lucky.game.exception.WalletException;
import bet.lucky.game.external_dto.response.UserTokenResponseDto;
import bet.lucky.game.internal_dto.AuthForm;
import bet.lucky.game.services.UserService;
import com.google.gson.Gson;
import io.sentry.Sentry;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/v1/user")
public class UserController {

    private final UserService userService;

    private final Gson gson;

    public UserController(UserService userService, Gson gson) {
        this.userService = userService;
        this.gson = gson;
    }

    @PostMapping(value = "auth", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Object> auth(@RequestBody AuthForm authForm, HttpServletRequest httpServletRequest) throws WalletException {
        Sentry.getContext().addExtra("request", authForm);

        UserTokenResponseDto user = userService.auth(authForm.getToken(), httpServletRequest);
        try {
            if (user.getData() != null && user.getData().size() > 0) {
                String username = user.getData().get(0).getUsername();
                int index = username.indexOf("@");
                if (index > 0) {
                    user.getData().get(0).setUsername(username.substring(0, index));
                }
            }
        } catch (Exception e) {
            Sentry.capture(e);
        }

        return new ResponseEntity<>(gson.toJson(user), HttpStatus.OK);
    }

}
