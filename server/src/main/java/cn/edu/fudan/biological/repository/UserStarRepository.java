package cn.edu.fudan.biological.repository;

import cn.edu.fudan.biological.domain.User_star;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * @program: biological
 * @description: 用户收藏仓库
 * @author: Yao Hongtao
 * @create: 2021-03-12 22:37
 **/
@Repository
public interface UserStarRepository extends CrudRepository<User_star, Integer> {
    Set<User_star> findAllByUserId(Integer userId);

    Set<User_star> findAllByQuestionnaireId(Integer questionnaireId);
}
