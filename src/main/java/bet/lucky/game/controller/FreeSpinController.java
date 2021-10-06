package bet.lucky.game.controller;

import bet.lucky.game.model.redis.UserRedis;
import bet.lucky.game.model.thirdparty.TicketBonus;
import bet.lucky.game.services.TicketBonusService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import bet.lucky.game.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/free/spin")
public class FreeSpinController {
    private final TicketBonusService ticketBonusService;

    private final UserService userService;

    private final Gson gson;

    @GetMapping(value = "/{token}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getFreeSpin(@PathVariable String token, HttpServletRequest httpServletRequest) {
        UserRedis user = userService.getUserByToken(token, httpServletRequest);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Vui lòng đăng nhập để quay!");
        }
        TicketBonus ticketBonus = ticketBonusService.getTicketBonus(user.getUid());
        if (ticketBonus == null) return new ResponseEntity<>(gson.toJson(new TicketBonus()), HttpStatus.OK);
        ticketBonus.setFreeSpin(ticketBonus.getTicket() - ticketBonus.getTicketUsed());
        return new ResponseEntity<>(gson.toJson(ticketBonus), HttpStatus.OK);
    }
}
