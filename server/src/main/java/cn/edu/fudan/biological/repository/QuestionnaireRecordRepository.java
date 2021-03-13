package cn.edu.fudan.biological.repository;

import cn.edu.fudan.biological.domain.Questionnaire_record;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface QuestionnaireRecordRepository extends CrudRepository<Questionnaire_record, Integer> {
    Set<Questionnaire_record> findAllByUserIdAndQuestionnaireId(Integer userId, Integer questionnaireId);
}
