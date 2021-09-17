package lucky.loteria.games.repository.impl;

import lucky.loteria.games.model.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Repository
public interface TableRepository extends JpaRepository<Table, Integer>{
    public List<Table> findTablesByStatusEquals(Integer status);
    public List<Table> findTablesByStatusEqualsOrderByIndexOrderAsc(Integer status);
    public List<Table> findTablesByGroupNameEqualsAndStatusEquals(String groupName, Integer status);
    public Table findTableByIdEquals(long id);
}
