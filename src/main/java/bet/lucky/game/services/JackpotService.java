package bet.lucky.game.services;

import bet.lucky.game.external_dto.response.JackpotResponse;
import bet.lucky.game.model.Jackpot;
import bet.lucky.game.repository.impl.JackpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JackpotService {
    private final JackpotRepository jackpotRepository;

    public JackpotResponse getJackpot() {
        List<Jackpot> lstJacpot = jackpotRepository.findAll();
        BigDecimal amount = lstJacpot.get(0).getJackpot();
        return JackpotResponse.builder().jackpot(amount).build();
    }

}
