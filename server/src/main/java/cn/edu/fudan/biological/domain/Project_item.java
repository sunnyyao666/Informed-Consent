package cn.edu.fudan.biological.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @program: biological
 * @description: 项目具体数据字段
 * @author: Yao Hongtao
 * @create: 2021-03-14 15:30
 **/
@Entity
public class Project_item extends BaseEntity {
    private Integer aid;

    @ManyToOne
    @JsonIgnore
    private Project_info projectInfo;
    private Integer pid;

    // 字段名
    private String name;
    private String description;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "projectItem")
    private Set<Agreement_response> responses = new HashSet<>();

    public Project_item() {
    }

    public Project_item(Project_info projectInfo, String name, String description) {
        this.projectInfo = projectInfo;
        this.pid = projectInfo.getId();
        this.name = name;
        this.description = description;
    }

    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer dataId) {
        this.aid = dataId;
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

    public String getName() {
        return name;
    }

    public void setName(String data) {
        this.name = data;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Agreement_response> getResponses() {
        return responses;
    }

    public void setResponses(Set<Agreement_response> responses) {
        this.responses = responses;
    }

  @Override
  public String toString() {
    return "Project_item{" +
        "aid=" + aid +
        ", projectInfo=" + projectInfo +
        ", pid=" + pid +
        ", name='" + name + '\'' +
        ", description='" + description + '\'' +
        ", responses=" + responses +
        '}';
  }
}

