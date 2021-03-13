package cn.edu.fudan.biological.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @program: biological
 * @description: 域权限等级
 * @author: Yao Hongtao
 * @create: 2021-03-13 13:26
 **/
@Entity
public class Authorization_level extends BaseEntity {
    private Integer levelNum;
    private String levelTitle;
    private String levelDescription;

    @ManyToOne
    private Questionnaire_field questionnaireField;
    private Integer fieldId;

    public Authorization_level() {
    }

    public Authorization_level(Integer levelNum, String levelTitle, String levelDescription, Questionnaire_field questionnaireField) {
        this.levelNum = levelNum;
        this.levelTitle = levelTitle;
        this.levelDescription = levelDescription;
        this.questionnaireField = questionnaireField;
        this.fieldId = questionnaireField.getId();
    }

    public Integer getLevelNum() {
        return levelNum;
    }

    public void setLevelNum(Integer levelNum) {
        this.levelNum = levelNum;
    }

    public String getLevelTitle() {
        return levelTitle;
    }

    public void setLevelTitle(String levelTitle) {
        this.levelTitle = levelTitle;
    }

    public String getLevelDescription() {
        return levelDescription;
    }

    public void setLevelDescription(String levelDescription) {
        this.levelDescription = levelDescription;
    }

    public Questionnaire_field getQuestionnaireField() {
        return questionnaireField;
    }

    public void setQuestionnaireField(Questionnaire_field questionnaireField) {
        this.questionnaireField = questionnaireField;
    }

    public Integer getFieldId() {
        return fieldId;
    }

    public void setFieldId(Integer fieldId) {
        this.fieldId = fieldId;
    }
}

