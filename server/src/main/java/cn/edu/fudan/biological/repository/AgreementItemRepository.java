package cn.edu.fudan.biological.repository;

import cn.edu.fudan.biological.domain.Agreement_item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgreementItemRepository extends CrudRepository<Agreement_item, Integer> {
    Agreement_item findByPidAndIid(Integer pid, Integer iid);

    List<Agreement_item> findAllByPidOrderByIid(Integer pid);

    void deleteAllByPid(Integer pid);
}
