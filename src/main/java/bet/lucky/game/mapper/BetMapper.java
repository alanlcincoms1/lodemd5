package bet.lucky.game.mapper;

import bet.lucky.game.external_dto.response.BetResponseDto;
import bet.lucky.game.model.Bet;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface BetMapper {
    List<BetResponseDto> mapToResponse(List<Bet> bet);
}
