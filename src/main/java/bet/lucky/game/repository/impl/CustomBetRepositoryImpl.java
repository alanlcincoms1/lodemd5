package bet.lucky.game.repository.impl;

import bet.lucky.game.constance.Constance;
import bet.lucky.game.model.BetTop;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class CustomBetRepositoryImpl implements CustomBetRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<BetTop> getTopBet() {
        String sql = "SELECT fullname, amount as stake, MAX(amount_win) AS amount " +
                " FROM bet " +
                " GROUP BY fullname order by created_date desc limit 100";

        Query query = em.createNativeQuery(sql, Constance.BET_TOP_MAPPING);
        return query.getResultList();
    }
}
