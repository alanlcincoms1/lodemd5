package bet.lucky.game.services;

import bet.lucky.game.external_dto.request.BetHistoryRequest;
import bet.lucky.game.external_dto.response.BetResponseDto;
import bet.lucky.game.external_dto.response.BetTopResponse;
import bet.lucky.game.external_dto.response.UserBalanceUpdateDto;
import bet.lucky.game.mapper.BetMapper;
import bet.lucky.game.model.Bet;
import bet.lucky.game.model.BetTop;
import bet.lucky.game.model.Transaction;
import bet.lucky.game.repository.impl.BetRepository;
import bet.lucky.game.repository.impl.TransactionRepository;
import io.sentry.Sentry;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BetService {

    private final BetRepository betRepository;

    private final TransactionRepository transactionRepository;

    private final UserService userService;

    private final BetMapper betMapper;

    public List<BetTopResponse> findTopBet() {
        List<BetTop> lstBet = betRepository.getTopBet();
        return betMapper.mapToTopBetResponse(lstBet);
    }

    public void updateBetsAfterResult(String[] status) {
        List<Bet> bets = betRepository.findAllByStatusInAndIsRunningEquals(
                status,
                Bet.RUNNING_STATUS.RUNNING.getValue()
        );
        for (Bet bet : bets) {
            transactionBet(bet);
        }
    }

    public void transactionBet(Bet bet) {
        try {
            //update bet
            bet.setUpdatedDate(new Date());
            bet = betRepository.save(bet);

            //create transaction
            Transaction transaction = new Transaction();
            transaction.setMemberId(bet.getMemberId());
            transaction.setAmount(bet.getAmount());
            transaction.setBet(bet);
            transaction.setType(bet.getStatus());
            transaction.setStatus(Transaction.TransactionStatus.NONE.name());
            transaction.setCreatedDate(new Date());
            transaction.setUpdatedDate(new Date());
            transactionRepository.save(transaction);

            UserBalanceUpdateDto userBalanceUpdateDto = userService.updateBalanceAfterBetResult(bet);
            if (userBalanceUpdateDto == null || userBalanceUpdateDto.getError_code() != 200) {
                transaction.setStatus(Transaction.TransactionStatus.FAIL.name());
                transaction.setNote(userBalanceUpdateDto == null ? "no response" : userBalanceUpdateDto.toString());
                transaction.setUpdatedDate(new Date());
                transactionRepository.save(transaction);

                //update bet
                bet.setIsRunning(Bet.RUNNING_STATUS.ERROR.getValue());
                bet.setUpdatedDate(new Date());
                betRepository.save(bet);
                return;
            }
            //update transaction
            updateTransactionAfterCallWallet(transaction, userBalanceUpdateDto);
            bet.setIsRunning(Bet.RUNNING_STATUS.FINISH.getValue());
            bet.setUpdatedDate(new Date());
            betRepository.save(bet);
        } catch (Exception e) {
            e.printStackTrace();
            Sentry.capture("Update WIN/LOSE Error: " + e.getMessage());
        }
    }

    public void updateBetsError(String[] status) {
        List<Bet> bets = betRepository.findAllByStatusInAndIsRunningEquals(
                status,
                Bet.RUNNING_STATUS.ERROR.getValue()
        );

        for (Bet bet : bets) {
            try {
                //update bet
                bet.setUpdatedDate(new Date());
                bet = betRepository.save(bet);

                //create transaction
                List<Transaction> transactions = transactionRepository.findTransactionsByBetIdAndType(bet.getId(), bet.getStatus());
                if (transactions == null || transactions.isEmpty()) {
                    continue;
                }

                Transaction transaction = transactions.get(0);
                UserBalanceUpdateDto userBalanceUpdateDto = userService.updateBalanceAfterBetResult(bet);

                if (userBalanceUpdateDto == null || (userBalanceUpdateDto.getError_code() != 200 && userBalanceUpdateDto.getError_code() != 409)) {
                    transaction.setStatus(Transaction.TransactionStatus.FAIL.name());
                    transaction.setNote(userBalanceUpdateDto == null ? "no response" : userBalanceUpdateDto.toString());
                    transaction.setUpdatedDate(new Date());
                    transactionRepository.save(transaction);
                    continue;
                }

                //update bet
                bet.setIsRunning(Bet.RUNNING_STATUS.FINISH.getValue());
                bet.setUpdatedDate(new Date());
                betRepository.save(bet);
                //update transaction
                transaction.setNote(userBalanceUpdateDto.toString());
                updateTransactionAfterCallWallet(transaction, userBalanceUpdateDto);
            } catch (Exception e) {
                e.printStackTrace();
                Sentry.capture("Update ERROR Error: " + e.getMessage());
            }

        }
    }

    public void updateTransactionAfterCallWallet(Transaction transactionJacpot, UserBalanceUpdateDto userBalanceUpdateDto) {
        transactionJacpot.setAmountAfter(userBalanceUpdateDto.getAmount_after());
        transactionJacpot.setAmountBefore(userBalanceUpdateDto.getAmount_before());
        transactionJacpot.setAgentTransactionId(userBalanceUpdateDto.getAgency_transaction_id());
        transactionJacpot.setReqAmount(userBalanceUpdateDto.getReq_amount());
        transactionJacpot.setDuesAmount(userBalanceUpdateDto.getDues_amount());
        transactionJacpot.setStatus(Transaction.TransactionStatus.SUCCESS.name());
        transactionJacpot.setUpdatedDate(new Date());
        transactionRepository.save(transactionJacpot);
    }

    public Map<String, Object> getPageBetHistory(BetHistoryRequest request) {
        Pageable paging = PageRequest.of(request.getPage() - 1, request.getSize());

        Page<Bet> pageBets = betRepository.findAllByFullname(request.getFullname(), paging);
        List<Bet> lstBets = pageBets.getContent();
        List<BetResponseDto> responseDtoList = betMapper.mapToResponse(lstBets);
        Map<String, Object> response = new HashMap<>();
        response.put("data", responseDtoList);
        response.put("total_items", pageBets.getTotalElements());
        response.put("total_pages", pageBets.getTotalPages());
        response.put("current_page", pageBets.getNumber() + 1);

        return response;
    }
}
