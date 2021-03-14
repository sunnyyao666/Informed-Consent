package cn.edu.fudan.biological.repository;

import cn.edu.fudan.biological.domain.Project_info;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectInfoRepository extends CrudRepository<Project_info, Integer> {
    Project_info findTopByPid(Integer pid);
}

