package cn.edu.fudan.biological.repository;

import cn.edu.fudan.biological.domain.Project_item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProjectItemRepository extends CrudRepository<Project_item, Integer> {
    Set<Project_item> findAllByPidOrderByAid(Integer pid);

    void deleteAllByPid(Integer pid);

    Project_item findByAid(Integer aid);
}
