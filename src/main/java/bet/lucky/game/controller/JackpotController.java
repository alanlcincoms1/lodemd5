package bet.lucky.game.controller;

import bet.lucky.game.external_dto.response.JackpotResponse;
import bet.lucky.game.services.JackpotService;
import bet.lucky.game.utils.ResponseFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/jackpot")
public class JackpotController {
    private final JackpotService jackpotService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JackpotResponse> getJackpot() {
        JackpotResponse jackpotResponse = jackpotService.getJackpot();
        return ResponseFactory.success(jackpotResponse);
    }

}
