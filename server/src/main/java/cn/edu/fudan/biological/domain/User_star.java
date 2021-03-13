package cn.edu.fudan.biological.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @program: biological
 * @description: 用户收藏
 * @author: Yao Hongtao
 * @create: 2021-03-12 20:32
 **/
@Entity
public class User_star extends BaseEntity {
    @ManyToOne
    private User_info userInfo;
    private Integer userId;

    @ManyToOne
    private Questionnaire_info questionnaireInfo;
    private Integer questionnaireId;

    public User_star() {
    }

    public User_star(User_info userInfo, Questionnaire_info questionnaireInfo) {
        this.userInfo = userInfo;
        this.userId = userInfo.getId();
        this.questionnaireInfo = questionnaireInfo;
        this.questionnaireId = questionnaireInfo.getId();
    }

    public User_info getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(User_info userInfo) {
        this.userInfo = userInfo;
        this.userId = userInfo.getId();
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
        this.questionnaireId = questionnaireInfo.getId();
    }

    public Integer getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(Integer questionnaireId) {
        this.questionnaireId = questionnaireId;
    }
}
