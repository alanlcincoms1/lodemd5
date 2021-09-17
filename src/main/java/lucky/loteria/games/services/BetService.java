package lucky.loteria.games.services;

import io.sentry.Sentry;
import lucky.loteria.games.external_dto.response.UserBalanceUpdateDto;
import lucky.loteria.games.model.Bet;
import lucky.loteria.games.model.Transaction;
import lucky.loteria.games.repository.impl.BetRepository;
import lucky.loteria.games.repository.impl.TransactionRepository;
import lucky.loteria.games.services.game_core.GameFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BetService {

    @Autowired
    BetRepository betRepository;

    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    GameFactory gameFactory;

    @Autowired
    UserService userService;

    public void updateBetsAfterResult(String[] status) {
        List<Bet> bets = betRepository.findAllByStatusInAndIsRunningEquals(
                status,
                Bet.RUNNING_STATUS.RUNNING.getValue()
        );
        for (Bet bet : bets) {
            try {
                //update bet
                bet.setUpdatedDate(new Date());
                bet = betRepository.save(bet);

                //create transaction
                Transaction transaction = new Transaction();
                transaction.setMemberId(bet.getMemberId());
                transaction.setAmount(bet.getAmountWin());
                transaction.setBet(bet);
                transaction.setType(bet.getStatus());
                transaction.setStatus(Transaction.TransactionStatus.NONE.name());
                transaction.setTransactionHash(System.currentTimeMillis() + "");
                transaction.setCreatedDate(new Date());
                transaction.setUpdatedDate(new Date());
                transactionRepository.save(transaction);

                UserBalanceUpdateDto userBalanceUpdateDto = userService.updateBalanceAfterBetResult(bet, transaction);

                if (userBalanceUpdateDto == null || userBalanceUpdateDto.getError_code() != 200) {
                    transaction.setStatus(Transaction.TransactionStatus.FAIL.name());
                    transaction.setNote(userBalanceUpdateDto == null ? "no response" : userBalanceUpdateDto.toString());
                    transaction.setUpdatedDate(new Date());
                    transactionRepository.save(transaction);

                    //update bet
                    bet.setIsRunning(Bet.RUNNING_STATUS.ERROR.getValue());
                    bet.setUpdatedDate(new Date());
                    betRepository.save(bet);
                    continue;
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
                UserBalanceUpdateDto userBalanceUpdateDto = userService.updateBalanceAfterBetResult(bet, transaction);

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
}
