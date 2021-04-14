package cn.edu.fudan.biological.repository;

import cn.edu.fudan.biological.domain.Agreement_item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgreementItemRepository extends CrudRepository<Agreement_item,Integer> {
    Agreement_item findByIid(Integer iid);
    void deleteAllByPid(Integer pid);
}
