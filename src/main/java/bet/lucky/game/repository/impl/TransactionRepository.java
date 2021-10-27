package bet.lucky.game.repository.impl;

import bet.lucky.game.model.Bet;
import bet.lucky.game.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    Transaction findAllByStatusAndTypeAndBet(String status, String type, Bet bet);

    @Query(value = "select * from (select * from transaction where bet_id = :betId) temp order by id desc", nativeQuery = true)
    List<Transaction> findTransactionsByBetId(long betId);

    @Query(value = "select * from transaction where bet_id = :betId and type = :typeTrans", nativeQuery = true)
    List<Transaction> findTransactionsByBetIdAndType(long betId, String typeTrans);
}
