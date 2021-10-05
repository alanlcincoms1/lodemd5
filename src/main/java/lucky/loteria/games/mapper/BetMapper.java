package lucky.loteria.games.mapper;

import lucky.loteria.games.external_dto.response.BetResponseDto;
import lucky.loteria.games.model.Bet;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface BetMapper {
    List<BetResponseDto> mapToResponse(List<Bet> bet);
}
