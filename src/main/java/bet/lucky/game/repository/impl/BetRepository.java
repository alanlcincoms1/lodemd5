package bet.lucky.game.repository.impl;

import bet.lucky.game.model.Bet;
import bet.lucky.game.model.IBetStatement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BetRepository extends JpaRepository<Bet, Integer>, CustomBetRepository {

    Page<Bet> findAllByUid(String uid, Pageable pageable);

    @Query(value = "select * from bet where uid = :uid and status = 'WIN' order by prize desc limit :limit",
            nativeQuery = true
    )
    List<Bet> findAllByUid(String uid, int limit);

    @Query(value = "select * from bet where status in :statuses and is_running = :isRunning limit 500",
            nativeQuery = true
    )
    List<Bet> findAllByStatusInAndIsRunningEquals(String[] statuses, int isRunning);

    List<Bet> findAllByStatusInAndIsRunningEqualsAndUidEquals(String[] statuses, int isRunning, String uid);

    Page<Bet> findAllByTableIdAndStatusIn(long tableId, String[] statuses, Pageable pageable);

    Page<Bet> findAllByCreatedDateGreaterThanAndUidEqualsAndCreatedDateLessThanEqualAndStatusIsNot(Date fromDate, String uid, Date toDate, String status, Pageable pageable);


    @Query(value = "SELECT " +
            "CAST(bet.created_date AS DATE) as betDate, " +
            "sum(amount) AS amountTotal, " +
            "sum(COALESCE(amount_win, 0)) AS amountWinTotal " +
            "FROM bet " +
            "WHERE uid = :uid AND status != 'NONE'" +
            "GROUP BY betDate " +
            "ORDER BY betDate DESC LIMIT 15",
            nativeQuery = true
    )
    List<IBetStatement> dailyStatement(String uid);

}
