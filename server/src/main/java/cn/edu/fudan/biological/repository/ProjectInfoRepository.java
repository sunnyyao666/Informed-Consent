package cn.edu.fudan.biological.repository;

import cn.edu.fudan.biological.domain.Organization_info;
import cn.edu.fudan.biological.domain.Project_info;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

@Repository
public interface ProjectInfoRepository extends CrudRepository<Project_info, Integer> {
    Set<Project_info> findAllByOrganization(String organization);
    Set<Project_info> findAllByNameAndStatus(String name,String status);
    Project_info findByName(String name);
    Set<Project_info> findAllByName(String name);
    Project_info findByNameAndStatusAndOrganization(String name,String status,String Organization);
//    Project_info findByPid(Integer pid);

    List<Project_info> findAllByOrganizationAndStatusOrderByName(String organization, String status);

    List<Project_info> findAllByOrderByUpdateTimeDesc();

    List<Project_info> findAllByOrderByHotDesc();

    List<Project_info> findAllByNameContainingOrPurposeContainingOrderByUpdateTimeDesc(String name, String purpose);

    List<Project_info> findAllByNameContainingOrPurposeContainingOrderByHotDesc(String name, String purpose);
    @Transactional
    void deleteAllByPid(Integer pid);
    @Transactional
    void deleteAllById(Integer id);
    @Transactional
    void deleteAll();
}

