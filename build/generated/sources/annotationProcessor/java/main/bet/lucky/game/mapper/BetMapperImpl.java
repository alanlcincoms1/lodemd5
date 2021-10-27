package bet.lucky.game.mapper;

import bet.lucky.game.external_dto.response.BetResponseDto;
import bet.lucky.game.external_dto.response.BetTopResponse;
import bet.lucky.game.model.Bet;
import bet.lucky.game.model.BetTop;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-10-27T12:38:52+0700",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 11.0.11 (Oracle Corporation)"
)
@Component
public class BetMapperImpl implements BetMapper {

    @Override
    public List<BetResponseDto> mapToResponse(List<Bet> bet) {
        if ( bet == null ) {
            return null;
        }

        List<BetResponseDto> list = new ArrayList<BetResponseDto>( bet.size() );
        for ( Bet bet1 : bet ) {
            list.add( betToBetResponseDto( bet1 ) );
        }

        return list;
    }

    @Override
    public List<BetTopResponse> mapToTopBetResponse(List<BetTop> bet) {
        if ( bet == null ) {
            return null;
        }

        List<BetTopResponse> list = new ArrayList<BetTopResponse>( bet.size() );
        for ( BetTop betTop : bet ) {
            list.add( betTopToBetTopResponse( betTop ) );
        }

        return list;
    }

    protected BetResponseDto betToBetResponseDto(Bet bet) {
        if ( bet == null ) {
            return null;
        }

        BetResponseDto betResponseDto = new BetResponseDto();

        betResponseDto.setId( bet.getId() );
        betResponseDto.setCreatedDate( bet.getCreatedDate() );
        betResponseDto.setUpdatedDate( bet.getUpdatedDate() );
        betResponseDto.setIp( bet.getIp() );
        betResponseDto.setUid( bet.getUid() );
        if ( bet.getMemberId() != null ) {
            betResponseDto.setMemberId( String.valueOf( bet.getMemberId() ) );
        }
        betResponseDto.setTableId( bet.getTableId() );
        betResponseDto.setAmount( bet.getAmount() );
        betResponseDto.setAmountWin( bet.getAmountWin() );
        betResponseDto.setReel( bet.getReel() );
        betResponseDto.setPrize( bet.getPrize() );
        betResponseDto.setStatus( bet.getStatus() );
        betResponseDto.setIsRunning( bet.getIsRunning() );
        betResponseDto.setFullname( bet.getFullname() );
        betResponseDto.setTransactionHash( bet.getTransactionHash() );

        return betResponseDto;
    }

    protected BetTopResponse betTopToBetTopResponse(BetTop betTop) {
        if ( betTop == null ) {
            return null;
        }

        BetTopResponse betTopResponse = new BetTopResponse();

        betTopResponse.setFullname( betTop.getFullname() );
        betTopResponse.setAmount( betTop.getAmount() );

        return betTopResponse;
    }
}
