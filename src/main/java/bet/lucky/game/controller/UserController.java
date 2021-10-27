package bet.lucky.game.controller;

import bet.lucky.game.exception.WalletException;
import bet.lucky.game.external_dto.response.UserBalanceDto;
import bet.lucky.game.external_dto.response.UserTokenResponseDto;
import bet.lucky.game.internal_dto.AuthForm;
import bet.lucky.game.services.UserService;
import bet.lucky.game.utils.ResponseFactory;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/v1/user")
@Slf4j
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
        log.info("auth request [{}]", authForm);
        UserTokenResponseDto user = userService.auth(authForm.getToken(), httpServletRequest);
        try {
            if (user.getData() != null && user.getData().size() > 0) {
                String fullname = user.getData().get(0).getFullname();
                int index = fullname.indexOf("@");
                if (index > 0) {
                    user.getData().get(0).setFullname(fullname.substring(0, index));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return new ResponseEntity<>(gson.toJson(user), HttpStatus.OK);
    }

    @GetMapping(value = "get-balance/{token}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<UserBalanceDto> getBalance(@PathVariable String token) {
        UserBalanceDto userBalanceDto = userService.getBalance(token);
        return ResponseFactory.success(userBalanceDto);
    }

}
