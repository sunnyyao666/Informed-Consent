package cn.edu.fudan.biological.repository;

import cn.edu.fudan.biological.domain.Agreement_info;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AgreementInfoRepository extends CrudRepository<Agreement_info, Integer> {
    Agreement_info findTopByUsernameAndPid(String username, Integer pid);

    Set<Agreement_info> findAllByPid(Integer pid);

    Set<Agreement_info> findAllByUsername(String username);
}
