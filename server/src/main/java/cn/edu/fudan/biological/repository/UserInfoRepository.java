package cn.edu.fudan.biological.repository;

import cn.edu.fudan.biological.domain.User_info;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends CrudRepository<User_info, Integer> {
    User_info findTopByOpenId(String openId);
}
