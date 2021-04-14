package cn.edu.fudan.biological.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @program: biological
 * @description: 用户填写的具体答案
 * @author: Yao Hongtao
 * @create: 2021-03-14 16:04
 **/
@Entity
public class Agreement_response extends BaseEntity {
    @ManyToOne
    @JsonIgnore
    private Agreement_info agreementInfo;
    private Integer agreementId;

    @ManyToOne
    @JsonIgnore
    private Project_item projectItem;
    private Integer aid;
    private Integer pid;

    @ManyToOne
    @JsonIgnore
    private Agreement_item agreementItem;
    private Integer iid;

    public Agreement_response() {
    }

    public Agreement_response(Agreement_info agreementInfo, Project_item projectItem, Agreement_item agreementItem) {
        this.agreementInfo = agreementInfo;
        this.agreementId = agreementInfo.getId();
        this.projectItem = projectItem;
        this.aid = projectItem.getAid();
        this.pid = agreementInfo.getPid();
        this.agreementItem = agreementItem;
        this.iid = agreementItem.getIid();
    }

    public Agreement_info getAgreementInfo() {
        return agreementInfo;
    }

    public void setAgreementInfo(Agreement_info agreementInfo) {
        this.agreementInfo = agreementInfo;
        this.agreementId = agreementInfo.getId();
        this.pid = agreementInfo.getPid();
    }

    public Integer getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(Integer agreementId) {
        this.agreementId = agreementId;
    }

    public Project_item getProjectItem() {
        return projectItem;
    }

    public void setProjectItem(Project_item projectItem) {
        this.projectItem = projectItem;
        this.aid = projectItem.getAid();
    }

    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Agreement_item getAgreementItem() {
        return agreementItem;
    }

    public void setAgreementItem(Agreement_item agreementItem) {
        this.agreementItem = agreementItem;
        this.iid = agreementItem.getIid();
    }

    public Integer getIid() {
        return iid;
    }

    public void setIid(Integer iid) {
        this.iid = iid;
    }
}

