package cn.edu.fudan.biological.repository;

import cn.edu.fudan.biological.domain.Questionnaire_info;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionnaireInfoRepository extends CrudRepository<Questionnaire_info, Integer> {
    Questionnaire_info findTopById(Integer id);
}

