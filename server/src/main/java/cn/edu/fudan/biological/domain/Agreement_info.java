package cn.edu.fudan.biological.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @program: biological
 * @description: 用户对项目的填写
 * @author: Yao Hongtao
 * @create: 2021-03-14 15:38
 **/
@Entity
public class Agreement_info extends BaseEntity {
    @ManyToOne
    @JsonIgnore
    private User_info userInfo;
    private String username;

    @ManyToOne
    @JsonIgnore
    private Project_info projectInfo;
    private Integer pid;

    private String status = "completed"; // completed, saved

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "agreementInfo")
    private Set<Agreement_response> responses = new HashSet<>();

    public Agreement_info() {
    }

    public Agreement_info(User_info userInfo, Project_info projectInfo) {
        this.userInfo = userInfo;
        this.username = userInfo.getUsername();
        this.projectInfo = projectInfo;
        this.pid = projectInfo.getPid();
    }

    public User_info getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(User_info userInfo) {
        this.userInfo = userInfo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Project_info getProjectInfo() {
        return projectInfo;
    }

    public void setProjectInfo(Project_info projectInfo) {
        this.projectInfo = projectInfo;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<Agreement_response> getResponses() {
        return responses;
    }

    public void setResponses(Set<Agreement_response> responses) {
        this.responses = responses;
    }
}

