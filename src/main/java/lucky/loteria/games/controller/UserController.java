package lucky.loteria.games.controller;

import com.google.gson.Gson;
import io.sentry.Sentry;
import lucky.loteria.games.exception.WalletException;
import lucky.loteria.games.external_dto.response.UserTokenResponseDto;
import lucky.loteria.games.internal_dto.AuthForm;
import lucky.loteria.games.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/v1/user")
public class UserController extends ExceptionHandle {

    private final UserService userService;

    private final Gson gson;

    public UserController(UserService userService, Gson gson) {
        this.userService = userService;
        this.gson = gson;
    }

    @PostMapping(value = "auth/")
    @ResponseBody
    public ResponseEntity<Object> auth(@RequestBody AuthForm authForm, HttpServletRequest httpServletRequest) throws WalletException {
        Sentry.getContext().addExtra("request", authForm);

        UserTokenResponseDto user = userService.auth(authForm.getToken(), httpServletRequest);

        if (user == null) {
            return new ResponseEntity<>(gson.toJson(user), HttpStatus.UNAUTHORIZED);
        }
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
