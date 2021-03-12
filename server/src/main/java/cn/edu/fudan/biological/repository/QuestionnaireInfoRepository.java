package cn.edu.fudan.biological.repository;

import cn.edu.fudan.biological.domain.Questionnaire_info;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @program: biological
 * @description: 问卷信息仓库
 * @author: Yao Hongtao
 * @create: 2021-03-12 22:37
 **/
@Repository
public interface QuestionnaireInfoRepository extends CrudRepository<Questionnaire_info, Integer> {
    Questionnaire_info findTopById(Integer id);
}

