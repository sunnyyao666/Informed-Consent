package cn.edu.fudan.biological.repository;

import cn.edu.fudan.biological.domain.User_star;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserStarRepository extends CrudRepository<User_star, Integer> {
    Set<User_star> findAllByUserId(Integer userId);

    Set<User_star> findAllByQuestionnaireId(Integer questionnaireId);
}
