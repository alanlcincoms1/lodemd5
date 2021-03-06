package bet.lucky.game.services;

import bet.lucky.game.constance.Constance;
import bet.lucky.game.exception.ApplicationException;
import bet.lucky.game.exception.message.UserMessage;
import bet.lucky.game.external_dto.request.BetDataRequest;
import bet.lucky.game.external_dto.request.Transfer;
import bet.lucky.game.external_dto.request.TransferBalanceRequest;
import bet.lucky.game.external_dto.response.*;
import bet.lucky.game.model.Bet;
import bet.lucky.game.model.SessionLog;
import bet.lucky.game.model.Tables;
import bet.lucky.game.model.User;
import bet.lucky.game.model.redis.UserRedis;
import bet.lucky.game.repository.impl.SessionLogRepository;
import bet.lucky.game.repository.impl.TableRepository;
import bet.lucky.game.repository.impl.UserRedisRepository;
import bet.lucky.game.repository.impl.UserRepository;
import bet.lucky.game.utils.ExternalRequestUtils;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    @Value("${auth_url}")
    private String verifyTokenURL;

    @Value("${update_balance_url}")
    private String updateBalanceURL;

    @Value("${get_balance_url}")
    private String getBalanceURL;

    @Value("${prefix_game}")
    private String prefixGame;

    private final UserRedisRepository userRedisRepository;

    private final SessionLogRepository sessionLogRepository;

    private final TableRepository tableRepository;

    private final UserRepository userRepository;

    private final Gson gson;

    public UserTokenResponseDto auth(String token, HttpServletRequest httpServletRequest) {
        UserTokenResponseDto userTokenResponseDto = getUser(token);
        if (userTokenResponseDto == null) {
            return null;
        }
        if (userTokenResponseDto.getData() == null || userTokenResponseDto.getData().isEmpty()) {
            return userTokenResponseDto;
        }
        UserTokenDto userTokenDto = userTokenResponseDto.getData().get(0);
        UserRedis userRedis = new UserRedis();
        BeanUtils.copyProperties(userTokenDto, userRedis);
        userRedis.setToken(token);

        // create User
        User user = userRepository.getUserByUidEquals(userRedis.getUid());
        if (user != null) {
            user.setStatus(userRedis.getStatus());
            user.setUpdatedDate(new Date());
            user.setBalance(userRedis.getMain_balance() + userRedis.getExtra_balance());
            user.setAgentId(userRedis.getAgency_id());
        } else {
            user = new User();
            user.setUid(userRedis.getUid());
            user.setAgentId(userRedis.getAgency_id());
            user.setMemberId(userRedis.getMember_id());
            user.setFullName(userRedis.getFullname());
            user.setBalance(userRedis.getMain_balance() + userRedis.getExtra_balance());
            user.setStatus(userRedis.getStatus());
            user.setCreatedDate(new Date());
            user.setUpdatedDate(new Date());
        }
        userRepository.save(user);
        userRedisRepository.save(userRedis);

        SessionLog sessionLog = new SessionLog();
        sessionLog.setMemberId(userRedis.getMember_id());
        sessionLog.setUid(userRedis.getUid());
        sessionLog.setIp(httpServletRequest.getRemoteAddr());
        sessionLog.setToken(token);
        sessionLog.setUserAgent(httpServletRequest.getHeader("User-Agent"));
        sessionLog.setCreatedDate(new Date());
        sessionLog.setUpdatedDate(new Date());
        sessionLogRepository.save(sessionLog);

        return userTokenResponseDto;
    }

    public UserTokenResponseDto getUser(String token) {
        UserTokenResponseDto userTokenResponseDto = (UserTokenResponseDto) ExternalRequestUtils.makeRequest(
                verifyTokenURL + token,
                "GET",
                null,
                UserTokenResponseDto.class
        );

        if (userTokenResponseDto == null || userTokenResponseDto.getData() == null || userTokenResponseDto.getData().isEmpty()) {
            throw new ApplicationException(UserMessage.UNAUTHORIZED);
        }
        return userTokenResponseDto;
    }

    public UserRedis getUserAndSaveToRedis(String token) {
        Optional<UserRedis> userRedisOptional = userRedisRepository.findById(token);
        UserRedis userRedis = new UserRedis();
        if (userRedisOptional.isEmpty()) {
            UserTokenResponseDto userTokenResponseDto = getUser(token);
            if (userTokenResponseDto == null) {
                return null;
            }
            UserTokenDto userTokenDto = userTokenResponseDto.getData().get(0);
            BeanUtils.copyProperties(userTokenDto, userRedis);
            userRedis.setToken(token);
            userRedisRepository.save(userRedis);
        } else {
            userRedis = userRedisOptional.get();
        }

        return userRedis;
    }

    public UserRedis getUserByToken(String token, HttpServletRequest httpServletRequest) {
        Optional<UserRedis> userRedisOptional = userRedisRepository.findById(token);
        UserRedis userRedis = null;
        if (userRedisOptional.isEmpty()) {
            UserTokenResponseDto userTokenResponseDto = getUser(token);

            if (userTokenResponseDto == null || userTokenResponseDto.getData() == null || userTokenResponseDto.getData().isEmpty()) {
                return null;
            }
            UserTokenDto userTokenDto = userTokenResponseDto.getData().get(0);
            userRedis = new UserRedis();
            BeanUtils.copyProperties(userTokenDto, userRedis);
            userRedis.setToken(token);
            // create User
            User user = userRepository.getUserByUidEquals(userRedis.getUid());
            if (user != null) {
                user.setStatus(userRedis.getStatus());
                user.setUpdatedDate(new Date());
                user.setBalance(userRedis.getMain_balance() + userRedis.getExtra_balance());
                user.setAgentId(userRedis.getAgency_id());
            } else {
                user = new User();
                user.setUid(userRedis.getUid());
                user.setAgentId(userRedis.getAgency_id());
                user.setMemberId(userRedis.getMember_id());
                user.setFullName(userRedis.getFullname());
                user.setBalance(userRedis.getMain_balance() + userRedis.getExtra_balance());
                user.setStatus(userRedis.getStatus());
                user.setCreatedDate(new Date());
                user.setUpdatedDate(new Date());
            }
            userRepository.save(user);
            userRedisRepository.save(userRedis);
        } else {
            userRedis = userRedisOptional.get();
        }

        return userRedis;
    }

    public UserBalanceDto getBalance(String token) {
        UserBalanceResponseDto userBalanceResponseDto = (UserBalanceResponseDto) ExternalRequestUtils.makeRequest(
                getBalanceURL + token,
                "GET",
                null,
                UserBalanceResponseDto.class
        );

        if (userBalanceResponseDto == null || userBalanceResponseDto.getData() == null || userBalanceResponseDto.getData().isEmpty()) {
            throw new ApplicationException(UserMessage.UNAUTHORIZED);
        }
        return userBalanceResponseDto.getData().get(0);
    }

    public UserBalanceUpdateDto updateBalanceAfterBetResult(Bet bet, String transactionId) {
        Tables tables = tableRepository.findTableByIdEquals(bet.getTableId());
        TransferBalanceRequest transferBalanceRequest = createTransferBalanceRequest(
                bet,
                tables,
                bet.getStatus(),
                transactionId
        );
        String params = gson.toJson(transferBalanceRequest);
        UserBalanceUpdateResponseDto userBalanceUpdateResponseDto = (UserBalanceUpdateResponseDto) ExternalRequestUtils.makeRequest(
                updateBalanceURL,
                "POST",
                params,
                UserBalanceUpdateResponseDto.class
        );
        if (userBalanceUpdateResponseDto == null) {
            return null;
        }

        if (CollectionUtils.isEmpty(userBalanceUpdateResponseDto.getData())) {
            log.error("g???i qua Wallet l???i n??y: " + userBalanceUpdateResponseDto.getMessage());
            return null;
        }

        return userBalanceUpdateResponseDto.getData().get(0);
    }

    private TransferBalanceRequest createTransferBalanceRequest(
            Bet bet,
            Tables tables,
            String action,
            String transactionId
    ) {
        TransferBalanceRequest transferBalanceRequest = new TransferBalanceRequest();
        transferBalanceRequest.setAgency_id(bet.getAgencyId());
        Transfer transfer = new Transfer();
        BetDataRequest betDataRequest = new BetDataRequest();

        transfer.setMember_id(bet.getMemberId());
        transfer.setUid(bet.getUid());
        double amount;
        switch (action) {
            case Constance.WIN:
                amount = bet.getAmountWin();
                transfer.setAction(Constance.WIN);
                betDataRequest.setGame_winlost(bet.getAmountWin() * Constance.DONGIA_VND);
                break;
            case Constance.LOSE:
                amount = bet.getAmountWin();
                transfer.setAction(Constance.WIN);
                betDataRequest.setGame_winlost(bet.getAmount() * Constance.DONGIA_VND);
                break;
            default:
                amount = bet.getAmount();
                transfer.setAction(Constance.BET);
                break;
        }
        transfer.setAmount(amount * Constance.DONGIA_VND);
        transfer.setTransaction_id(transactionId);

        betDataRequest.setGame_id(prefixGame + tables.getGameId());
        betDataRequest.setGame_name(tables.getName());
        betDataRequest.setGame_round_id(prefixGame + bet.getId());
        betDataRequest.setGame_ticket_id(prefixGame + bet.getId());
        betDataRequest.setGame_ticket_status(action);
        betDataRequest.setGame_stake(bet.getAmount() * Constance.DONGIA_VND);
        betDataRequest.setIp(bet.getIp());
        transfer.setData(betDataRequest);

        List<Transfer> lstTransfer = new ArrayList<>();
        lstTransfer.add(transfer);
        transferBalanceRequest.setTransfers(lstTransfer);
        return transferBalanceRequest;
    }

}
