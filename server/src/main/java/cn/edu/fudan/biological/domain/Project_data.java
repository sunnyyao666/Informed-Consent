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
public class Project_data extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer dataId;

    @ManyToOne
    @JsonIgnore
    private Project_info projectInfo;
    private Integer pid;

    private String data; // 字段名

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "projectData")
    private Set<Agreement_response> responses = new HashSet<>();

    public Project_data() {
    }

    public Project_data(Project_info projectInfo, String data) {
        this.projectInfo = projectInfo;
        this.pid = projectInfo.getPid();
        this.data = data;
    }

    public Integer getDataId() {
        return dataId;
    }

    public void setDataId(Integer dataId) {
        this.dataId = dataId;
    }

    public Project_info getProjectInfo() {
        return projectInfo;
    }

    public void setProjectInfo(Project_info projectInfo) {
        this.projectInfo = projectInfo;
        this.pid = projectInfo.getPid();
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Set<Agreement_response> getResponses() {
        return responses;
    }

    public void setResponses(Set<Agreement_response> responses) {
        this.responses = responses;
    }

    @Override
    public String toString() {
        return "Project_data{" +
                "dataId=" + dataId +
                ", projectInfo=" + projectInfo +
                ", pid=" + pid +
                ", data='" + data + '\'' +
                ", responses=" + responses +
                '}';
    }
}

