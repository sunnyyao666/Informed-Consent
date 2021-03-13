package cn.edu.fudan.biological.domain;

import javax.persistence.*;

/**
 * @program: biological
 * @description: 用户问卷签名图片
 * @author: Yao Hongtao
 * @create: 2021-03-12 19:54
 **/
@Entity
public class User_signature extends BaseEntity {
    @ManyToOne
    private User_info userInfo;
    private Integer userId;

    private String signatureAddress;

    @ManyToOne
    private Questionnaire_info questionnaireInfo;
    private Integer questionnaireId;

    public User_signature() {
    }

    public User_signature(User_info userInfo, String signatureAddress, Questionnaire_info questionnaireInfo) {
        this.userInfo = userInfo;
        this.userId = userInfo.getId();
        this.signatureAddress = signatureAddress;
        this.questionnaireInfo = questionnaireInfo;
        this.questionnaireId = questionnaireInfo.getId();
    }

    public User_info getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(User_info user_info) {
        this.userInfo = user_info;
        this.userId = user_info.getId();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getSignatureAddress() {
        return signatureAddress;
    }

    public void setSignatureAddress(String signature_address) {
        this.signatureAddress = signature_address;
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
