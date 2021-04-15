package cn.edu.fudan.biological.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @program: biological
 * @description: 项目字段同意类型
 * @author: Yao Hongtao
 * @create: 2021-04-13 19:59
 **/
@Entity
public class Agreement_item extends BaseEntity {
    private Integer iid;

    @ManyToOne
    @JsonIgnore
    private Project_info projectInfo;
    private Integer pid;

    private String name;
    private String value;
    private String description;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "agreementItem")
    private Set<Agreement_response> responses = new HashSet<>();

    public Agreement_item() {
    }

    public Agreement_item(Project_info projectInfo, String name, String value, String description) {
        this.projectInfo = projectInfo;
        this.pid = projectInfo.getId();
        this.name = name;
        this.value = value;
        this.description = description;
    }

    public Integer getIid() {
        return iid;
    }

    public void setIid(Integer iid) {
        this.iid = iid;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
}

