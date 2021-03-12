package cn.edu.fudan.biological.repository;

import cn.edu.fudan.biological.domain.User_signature;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * @program: biological
 * @description: 用户签名仓库
 * @author: Yao Hongtao
 * @create: 2021-03-12 22:37
 **/
@Repository
public interface UserSignatureRepository extends CrudRepository<User_signature, Integer> {
    Set<User_signature> findAllByUserId(Integer userId);

    Set<User_signature> findAllByQuestionnaireId(Integer questionnaireId);
}
