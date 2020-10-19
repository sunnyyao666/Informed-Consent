package cn.edu.fudan.biological.repository;

import cn.edu.fudan.biological.domain.user_info;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends CrudRepository<user_info, Integer> {
    user_info findTopByOpenid(String openid);
}
