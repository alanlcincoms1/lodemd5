package bet.lucky.game.repository.impl;

import bet.lucky.game.model.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableRepository extends JpaRepository<Table, Integer> {

    List<Table> findTablesByStatusEquals(Integer status);

    Table findTableByIdEquals(long id);
}
