package cn.edu.fudan.biological.repository;

import cn.edu.fudan.biological.domain.User_star;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserStarRepository extends CrudRepository<User_star, Integer> {
    List<User_star> findAllByUsernameOrderByPid(String username);

    Set<User_star> findAllByPid(Integer pid);

    User_star findByUsernameAndPid(String username, Integer pid);
}
