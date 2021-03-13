package cn.edu.fudan.biological.repository;

import cn.edu.fudan.biological.domain.Questionnaire_field;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface QuestionnaireFieldRepository extends CrudRepository<Questionnaire_field, Integer> {
    Set<Questionnaire_field> findAllByQuestionnaireId(Integer questionnaireId);
}
