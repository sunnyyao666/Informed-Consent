package cn.edu.fudan.biological.repository;

import cn.edu.fudan.biological.domain.Agreement_response;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.transaction.Transactional;

@Repository
public interface AgreementResponseRepository extends CrudRepository<Agreement_response, Integer> {
    List<Agreement_response> findAllByAgreementIdOrderByAid(Integer aid);

    @Transactional
    void deleteAllByPid(Integer pid);

    Agreement_response findByAgreementIdAndAid(Integer agreementId, Integer aid);
}
