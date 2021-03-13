package cn.edu.fudan.biological.repository;

import cn.edu.fudan.biological.domain.Organization_info;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationInfoRepository extends CrudRepository<Organization_info, Integer> {
    Organization_info findTopById(Integer id);
}
