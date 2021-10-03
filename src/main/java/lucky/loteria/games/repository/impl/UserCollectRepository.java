package lucky.loteria.games.repository.impl;

import lucky.loteria.games.model.ILuckyTop;
import lucky.loteria.games.model.UserCollect;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCollectRepository extends JpaRepository<UserCollect, String> {
    UserCollect findByTableIdAndUid(long tableId, String uid);

    Page<ILuckyTop> findAllByTableId(long tableId, Pageable pageable);
}
