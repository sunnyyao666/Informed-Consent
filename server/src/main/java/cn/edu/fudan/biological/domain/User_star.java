package cn.edu.fudan.biological.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @JsonIgnore
    private User_info userInfo;
    private Integer userId;

    @ManyToOne
    @JsonIgnore
    private Project_info projectInfo;
    private Integer pid;

    public User_star() {
    }

    public User_star(User_info userInfo, Project_info projectInfo) {
        this.userInfo = userInfo;
        this.userId = userInfo.getId();
        this.projectInfo = projectInfo;
        this.pid = projectInfo.getPid();
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

    public Project_info getProjectInfo() {
        return projectInfo;
    }

    public void setProjectInfo(Project_info projectInfo) {
        this.projectInfo = projectInfo;
        this.pid = projectInfo.getId();
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }
}
