package bet.lucky.game.repository.impl;

import bet.lucky.game.model.UserCollect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCollectRepository extends JpaRepository<UserCollect, String> {

    UserCollect findByTableIdAndUid(long tableId, String uid);

    UserCollect findByUid(String uid);
}
