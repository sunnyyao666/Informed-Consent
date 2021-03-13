package cn.edu.fudan.biological.repository;

import cn.edu.fudan.biological.domain.Authorization_level;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AuthorizationLevelRepository extends CrudRepository<Authorization_level, Integer> {
    Set<Authorization_level> findAllByFieldId(Integer fieldId);
}
