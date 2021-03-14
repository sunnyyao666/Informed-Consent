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
    private Project_data projectData;
    private Integer dataId;
    private Integer pid;

    private String response; // yes, no

    public Agreement_response() {
    }

    public Agreement_response(Agreement_info agreementInfo, Project_data projectData, String response) {
        this.agreementInfo = agreementInfo;
        this.agreementId = agreementInfo.getId();
        this.projectData = projectData;
        this.dataId = projectData.getDataId();
        this.pid = agreementInfo.getPid();
        this.response = response;
    }

    public Agreement_info getAgreementInfo() {
        return agreementInfo;
    }

    public void setAgreementInfo(Agreement_info agreementInfo) {
        this.agreementInfo = agreementInfo;
    }

    public Integer getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(Integer agreementId) {
        this.agreementId = agreementId;
    }

    public Project_data getProjectData() {
        return projectData;
    }

    public void setProjectData(Project_data projectData) {
        this.projectData = projectData;
    }

    public Integer getDataId() {
        return dataId;
    }

    public void setDataId(Integer dataId) {
        this.dataId = dataId;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}

