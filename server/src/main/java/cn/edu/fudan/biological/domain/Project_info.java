package cn.edu.fudan.biological.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @program: biological
 * @description: 项目信息
 * @author: Yao Hongtao
 * @create: 2021-03-12 20:42
 **/
@Entity
public class Project_info extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer pid;

    private String name;
    private Integer hot = 0;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endTime;

    private String purpose;

    private String status = "draft"; // finished, ongoing, draft

    @ManyToOne
    @JsonIgnore
    private Organization_info organizationInfo;
    private String organization;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "projectInfo")
    private Set<User_star> stars = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "projectInfo")
    private Set<Project_data> data = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "projectInfo")
    private Set<Agreement_info> agreements = new HashSet<>();

    public Project_info() {
    }

    public Project_info(Date startTime, Date endTime, String purpose, Organization_info organizationInfo) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.purpose = purpose;
        this.organizationInfo = organizationInfo;
        this.organization = organizationInfo.getOrganization();
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

    public Integer getHot() {
        return hot;
    }

    public void setHot(Integer hot) {
        this.hot = hot;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Organization_info getOrganizationInfo() {
        return organizationInfo;
    }

    public void setOrganizationInfo(Organization_info organizationInfo) {
        this.organizationInfo = organizationInfo;
        this.organization = organizationInfo.getOrganization();
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public Set<User_star> getStars() {
        return stars;
    }

    public void setStars(Set<User_star> stars) {
        this.stars = stars;
    }

    public Set<Project_data> getData() {
        return data;
    }

    public void setData(Set<Project_data> data) {
        this.data = data;
    }

    public Set<Agreement_info> getAgreements() {
        return agreements;
    }

    public void setAgreements(Set<Agreement_info> agreements) {
        this.agreements = agreements;
    }

    @Override
    public String toString() {
        return "Project_info{" +
                "pid=" + pid +
                ", name='" + name + '\'' +
                ", hot=" + hot +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", purpose='" + purpose + '\'' +
                ", status='" + status + '\'' +
                ", organizationInfo=" + organizationInfo +
                ", organization='" + organization + '\'' +
                ", stars=" + stars +
                ", data=" + data +
                ", agreements=" + agreements +
                '}';
    }
}


