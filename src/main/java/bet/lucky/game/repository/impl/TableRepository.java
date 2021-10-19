package bet.lucky.game.repository.impl;

import bet.lucky.game.model.Tables;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableRepository extends JpaRepository<Tables, Integer> {

    List<Tables> findTablesByStatusEquals(Integer status);

    Tables findTableByIdEquals(long id);
}
