package cn.edu.fudan.biological.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @program: biological
 * @description: 用户填写问卷记录
 * @author: Yao Hongtao
 * @create: 2021-03-13 13:37
 **/
@Entity
public class Questionnaire_record extends BaseEntity {
    @ManyToOne
    private User_info userInfo;
    private Integer userId;

    @ManyToOne
    private Questionnaire_info questionnaireInfo;
    private Integer questionnaireId;

    private Integer fieldId;

    private Integer levelId;

    public Questionnaire_record() {
    }

    public Questionnaire_record(User_info userInfo, Questionnaire_info questionnaireInfo, Integer fieldId, Integer levelId) {
        this.userInfo = userInfo;
        this.userId = userInfo.getId();
        this.questionnaireInfo = questionnaireInfo;
        this.questionnaireId = questionnaireInfo.getId();
        this.fieldId = fieldId;
        this.levelId = levelId;
    }

    public User_info getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(User_info userInfo) {
        this.userInfo = userInfo;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Questionnaire_info getQuestionnaireInfo() {
        return questionnaireInfo;
    }

    public void setQuestionnaireInfo(Questionnaire_info questionnaireInfo) {
        this.questionnaireInfo = questionnaireInfo;
    }

    public Integer getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(Integer questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public Integer getFieldId() {
        return fieldId;
    }

    public void setFieldId(Integer fieldId) {
        this.fieldId = fieldId;
    }

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }
}

