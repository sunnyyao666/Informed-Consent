package cn.edu.fudan.biological.repository;

import cn.edu.fudan.biological.domain.Agreement_response;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AgreementResponseRepository extends CrudRepository<Agreement_response, Integer> {
    Set<Agreement_response> findAllByAgreementIdOrderByDataId(Integer agreementId);
}
