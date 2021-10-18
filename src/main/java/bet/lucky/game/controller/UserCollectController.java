package bet.lucky.game.controller;

import bet.lucky.game.exception.ApplicationException;
import bet.lucky.game.exception.message.UserMessage;
import bet.lucky.game.external_dto.response.JackpotResponse;
import bet.lucky.game.model.Table;
import bet.lucky.game.model.UserCollect;
import bet.lucky.game.model.redis.UserRedis;
import bet.lucky.game.repository.impl.TableRepository;
import bet.lucky.game.repository.impl.UserCollectRepository;
import bet.lucky.game.services.UserService;
import bet.lucky.game.utils.ResponseFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/collects")
public class UserCollectController {

    private final UserService userService;
    private final TableRepository tableRepository;
    private final UserCollectRepository userCollectRepository;

    @GetMapping(value = "/{token}/{tableId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JackpotResponse> getJackpot(@PathVariable String token, @PathVariable long tableId, HttpServletRequest httpServletRequest) {
        UserRedis user = userService.getUserByToken(token, httpServletRequest);
        if (user == null) {
            throw new ApplicationException(UserMessage.UNAUTHORIZED);
        }
        Double jackPot;
        UserCollect userCollect = userCollectRepository.findByUid(user.getUid());
        if (userCollect == null) {
            Table table = tableRepository.findTableByIdEquals(tableId);
            jackPot = table.getInitJackpotAmount();
        } else {
            jackPot = userCollect.getJackpot();
        }
        JackpotResponse jackpotResponse = JackpotResponse.builder().jackpot(jackPot).build();
        return ResponseFactory.success(jackpotResponse);
    }

}
