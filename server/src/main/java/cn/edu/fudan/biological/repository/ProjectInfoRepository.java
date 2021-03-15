package cn.edu.fudan.biological.repository;

import cn.edu.fudan.biological.domain.Organization_info;
import cn.edu.fudan.biological.domain.Project_info;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProjectInfoRepository extends CrudRepository<Project_info, Integer> {
    Project_info findTopByPid(Integer pid);

    Set<Project_info> findAllByOrganization(String organization);

    Project_info findByName(String name);

    Project_info findByPid(Integer pid);

    List<Project_info> findAllByOrganizationAndStatusOrderByName(String organization, String status);
}

