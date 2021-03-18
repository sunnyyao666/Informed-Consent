package cn.edu.fudan.biological.repository;

import cn.edu.fudan.biological.domain.Agreement_response;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgreementResponseRepository extends CrudRepository<Agreement_response, Integer> {
    List<Agreement_response> findAllByAgreementIdOrderByDataId(Integer agreementId);

    Agreement_response findByAgreementIdAndDataId(Integer agreementId, Integer dataId);
}
