package bet.lucky.game.repository.impl;

import bet.lucky.game.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public User getUserByUidEquals(String uid);
}
