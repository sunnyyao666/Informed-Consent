package cn.edu.fudan.biological.repository;

import cn.edu.fudan.biological.domain.Project_data;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProjectDataRepository extends CrudRepository<Project_data, Integer> {
    Set<Project_data> findAllByPidOrderByDataId(Integer pid);
}
