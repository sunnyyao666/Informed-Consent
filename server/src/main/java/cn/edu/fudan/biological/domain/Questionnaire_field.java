package cn.edu.fudan.biological.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @program: biological
 * @description: 问卷隐私域
 * @author: Yao Hongtao
 * @create: 2021-03-13 13:18
 **/
@Entity
public class Questionnaire_field extends BaseEntity {
    @ManyToOne
    private Questionnaire_info questionnaireInfo;
    private Integer questionnaireId;

    private String fieldName;

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER, mappedBy = "questionnaireField")
    private Set<Authorization_level> levels = new HashSet<>();

    public Questionnaire_field() {
    }

    public Questionnaire_field(Questionnaire_info questionnaireInfo, String fieldName) {
        this.questionnaireInfo = questionnaireInfo;
        this.questionnaireId = questionnaireInfo.getId();
        this.fieldName = fieldName;
    }

    public Questionnaire_info getQuestionnaireInfo() {
        return questionnaireInfo;
    }

    public void setQuestionnaireInfo(Questionnaire_info questionnaireInfo) {
        this.questionnaireInfo = questionnaireInfo;
        this.questionnaireId = questionnaireInfo.getId();
    }

    public Integer getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(Integer questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Set<Authorization_level> getLevels() {
        return levels;
    }

    public void setLevels(Set<Authorization_level> levels) {
        this.levels = levels;
    }
}

