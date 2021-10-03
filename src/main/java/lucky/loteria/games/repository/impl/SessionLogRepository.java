package lucky.loteria.games.repository.impl;

import lucky.loteria.games.model.SessionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionLogRepository extends JpaRepository<SessionLog, Integer> {
}
