package cn.edu.fudan.biological.repository;

import cn.edu.fudan.biological.domain.Project_item;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.transaction.Transactional;

@Repository
public interface ProjectItemRepository extends CrudRepository<Project_item, Integer> {

  List<Project_item> findAllByPidOrderByAid(Integer pid);

  @Transactional
  void deleteAllByPid(Integer pid);

  Project_item findByPidAndAid(Integer pid, Integer aid);
}
