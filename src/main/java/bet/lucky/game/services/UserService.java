package bet.lucky.game.services;

import bet.lucky.game.exception.ApplicationException;
import bet.lucky.game.exception.WalletException;
import bet.lucky.game.exception.message.UserMessage;
import bet.lucky.game.external_dto.request.BetDataRequest;
import bet.lucky.game.external_dto.request.TransferBalanceRequest;
import bet.lucky.game.external_dto.response.UserBalanceUpdateDto;
import bet.lucky.game.external_dto.response.UserBalanceUpdateResponseDto;
import bet.lucky.game.external_dto.response.UserTokenDto;
import bet.lucky.game.external_dto.response.UserTokenResponseDto;
import bet.lucky.game.model.*;
import bet.lucky.game.model.redis.UserRedis;
import bet.lucky.game.repository.impl.SessionLogRepository;
import bet.lucky.game.repository.impl.TableRepository;
import bet.lucky.game.repository.impl.UserRedisRepository;
import bet.lucky.game.repository.impl.UserRepository;
import bet.lucky.game.utils.ExternalRequestUtils;
import com.google.gson.Gson;
import io.sentry.Sentry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    @Value("${auth_url}")
    private String verifyTokenURL;

    @Value("${update_balance_url}")
    private String updateBalanceURL;

    @Value("${bo_auth_url}")
    private String boAuthUrl;

    @Value("${prefix_game}")
    private String prefixGame;


    private final UserRedisRepository userRedisRepository;

    private final SessionLogRepository sessionLogRepository;

    private final TableRepository tableRepository;

    private final UserRepository userRepository;

    private final Gson gson;

    public static int DONGIA_VND = 1000;

    public UserTokenResponseDto auth(String token, HttpServletRequest httpServletRequest) {
        UserTokenResponseDto userTokenResponseDto = getUser(token);
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
            user.setAgentCode(userRedis.getAgency_code());
            user.setAgentCodeId(userRedis.getAgency_id() + "");
        } else {
            user = new User();
            user.setUid(userRedis.getUid());
            user.setAgentCode(userRedis.getAgency_code());
            user.setAgentCodeId(userRedis.getAgency_id() + "");
            user.setMemberId(userRedis.getMember_id());
            user.setFullName(userRedis.getFullname());
            user.setUsername(userRedis.getUsername());
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

    private UserTokenResponseDto createUserResponse(String token) {
        UserTokenResponseDto userTokenResponseDto = new UserTokenResponseDto();
        List<UserTokenDto> userTokenDtoList = new ArrayList<>();
        UserTokenDto userTokenDto = new UserTokenDto();
        userTokenDto.setMain_balance(100.0);
        userTokenDto.setExtra_balance(100.0);
        userTokenDto.setTime(100000L);
        userTokenDto.setFullname("dev daniel");
        userTokenDto.setAvatar("avatar-05");
        userTokenDto.setGroup("group1");
        userTokenDto.setAff_id("aff_id1");
        userTokenDto.setG_id("g_id1");
        userTokenDto.setUid("Uid1");
        userTokenDto.setMember_id("1");
        userTokenDto.setUsername("daniel");
        userTokenDto.setLast_login(new Date());
        userTokenDto.setExpried(new Date());
        userTokenDto.setToken(token);
        userTokenDto.setStatus("ACTIVE");
        userTokenDto.setAgency_code("14");
        userTokenDto.setAgency_id(1);
        userTokenDtoList.add(userTokenDto);
        userTokenResponseDto.setData(userTokenDtoList);
        return userTokenResponseDto;
    }

    public UserTokenResponseDto getUser(String token) {
        UserTokenResponseDto userTokenResponseDto = createUserResponse(token);
//        UserTokenResponseDto userTokenResponseDto = (UserTokenResponseDto) ExternalRequestUtils.makeRequest(
//                verifyTokenURL + token,
//                "GET",
//                null,
//                UserTokenResponseDto.class
//        );

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
            userRedis = getUserAndSaveToRedis(token);
            if (userRedis == null) {
                return null;
            }
            // create User
            User user = userRepository.getUserByUidEquals(userRedis.getUid());
            if (user != null) {
                user.setStatus(userRedis.getStatus());
                user.setUpdatedDate(new Date());
                user.setBalance(userRedis.getMain_balance() + userRedis.getExtra_balance());
                user.setAgentCode(userRedis.getAgency_code());
                user.setAgentCodeId(userRedis.getAgency_id() + "");
            } else {
                user = new User();
                user.setUid(userRedis.getUid());
                user.setAgentCodeId(userRedis.getAgency_id() + "");
                user.setAgentCode(userRedis.getAgency_code());
                user.setMemberId(userRedis.getMember_id());
                user.setFullName(userRedis.getFullname());
                user.setUsername(userRedis.getUsername());
                user.setBalance(userRedis.getMain_balance() + userRedis.getExtra_balance());
                user.setStatus(userRedis.getStatus());
                user.setCreatedDate(new Date());
                user.setUpdatedDate(new Date());
            }
            userRepository.save(user);
        } else {
            userRedis = userRedisOptional.get();
        }

        return userRedis;
    }

    public UserBalanceUpdateDto updateBalanceWhenBet(String token, Bet bet, Transaction transaction, Table table) throws WalletException {

        TransferBalanceRequest transferBalanceRequest = createTransferBalanceRequest(
                bet,
                transaction,
                table,
                token,
                Bet.BetStatus.BET.name()
        );
        List<TransferBalanceRequest> paramObject = new ArrayList<TransferBalanceRequest>();
        paramObject.add(transferBalanceRequest);
        String params = gson.toJson(paramObject);


        UserBalanceUpdateResponseDto userBalanceUpdateResponseDto = (UserBalanceUpdateResponseDto) ExternalRequestUtils.makeRequest(
                updateBalanceURL,
                "POST",
                params,
                UserBalanceUpdateResponseDto.class
        );
        if (userBalanceUpdateResponseDto == null) {
            throw new WalletException("Vui lòng thử lại!");
        }

        if (userBalanceUpdateResponseDto.getData() == null || userBalanceUpdateResponseDto.getData().isEmpty()) {
            throw new WalletException("Vui lòng thử lại!!");
        }

        return userBalanceUpdateResponseDto.getData().get(0);
    }


    public UserBalanceUpdateDto updateBalanceAfterBetResult(Bet bet, Transaction transaction) {
        Table table = tableRepository.findTableByIdEquals(bet.getTableId());
        TransferBalanceRequest transferBalanceRequest = createTransferBalanceRequest(
                bet,
                transaction,
                table,
                "",
                bet.getStatus()
        );
        List<TransferBalanceRequest> paramObject = new ArrayList<TransferBalanceRequest>();
        paramObject.add(transferBalanceRequest);
        String params = gson.toJson(paramObject);

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
            Sentry.capture("gọi qua Wallet lỗi này: " + userBalanceUpdateResponseDto.getMessage());
            return null;
        }

        return userBalanceUpdateResponseDto.getData().get(0);
    }

    private TransferBalanceRequest createTransferBalanceRequest(
            Bet bet,
            Transaction transaction,
            Table table,
            String token,
            String action
    ) {
        TransferBalanceRequest transferBalanceRequest = new TransferBalanceRequest();
        if (token.length() > 0) {
            transferBalanceRequest.setToken(token);
        } else {
            transferBalanceRequest.setUid(bet.getUid());
        }
        String status = bet.getStatus();
        double amount = 0.0;
        switch (status) {
            case "WIN":
                amount = bet.getAmount() + bet.getAmountWin();
                break;
            case "LOSE":
                amount = bet.getAmount() + bet.getAmountLose();
                break;
            case "BET":
                amount = bet.getAmount();
                break;
            default:
                break;
        }
        transferBalanceRequest.setAmount(amount * DONGIA_VND);
        transferBalanceRequest.setTransaction_id(transaction.getTransactionHash() + "." + transaction.getId());
        transferBalanceRequest.setAction(action);

        BetDataRequest betDataRequest = new BetDataRequest();
        betDataRequest.setGame_id(prefixGame + table.getGroupName());
        betDataRequest.setGame_name(table.getName());
        betDataRequest.setGame_round_id(table.toString());
        betDataRequest.setGame_ticket_id(bet.getId().toString());
        betDataRequest.setGame_ticket_status(action);
        betDataRequest.setGame_winlost((amount - bet.getAmount()) * DONGIA_VND);
        betDataRequest.setGame_stake(bet.getAmount() * DONGIA_VND);

        transferBalanceRequest.setData(betDataRequest);
        return transferBalanceRequest;
    }

    public User getUserByUid(String uid) {
        return userRepository.getUserByUidEquals(uid);
    }

}