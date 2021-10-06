package bet.lucky.game.repository.impl;

import bet.lucky.game.model.ILuckyTop;
import bet.lucky.game.model.UserCollect;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCollectRepository extends JpaRepository<UserCollect, String> {
    UserCollect findByTableIdAndUid(long tableId, String uid);

    Page<ILuckyTop> findAllByTableId(long tableId, Pageable pageable);
}
