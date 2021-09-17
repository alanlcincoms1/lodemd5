package lucky.loteria.games.repository.impl;

import lucky.loteria.games.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User getUserByUidEquals(String uid);
}
