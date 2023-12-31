package cn.edu.fudan.biological.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;

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

  private String name;

  private Integer hot = 0;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
  private Date startTime;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
  private Date endTime;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
  private Date releaseTime;

  @Lob
  @Basic(fetch = FetchType.LAZY)
  private String purpose;

  // finished, ongoing, draft
  private String status = "draft";

  @ManyToOne
  @JsonIgnore
  private Organization_info organizationInfo;

  private String organization;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "projectInfo")
  private Set<Project_item> projectItems = new HashSet<>();

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "projectInfo")
  private Set<Agreement_info> agreements = new HashSet<>();

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "projectInfo")
  private Set<Agreement_item> agreementItems = new HashSet<>();

  public Project_info() {
  }

  public Project_info(Date startTime, Date endTime, String purpose, Organization_info organizationInfo) {
    this.startTime = startTime;
    this.endTime = endTime;
    this.purpose = purpose;
    this.organizationInfo = organizationInfo;
    this.organization = organizationInfo.getOrganization();
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

  public Date getReleaseTime() {
    return releaseTime;
  }

  public void setReleaseTime(Date releaseTime) {
    this.releaseTime = releaseTime;
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

  public Set<Project_item> getProjectItems() {
    return projectItems;
  }

  public void setProjectItems(Set<Project_item> data) {
    this.projectItems = data;
  }

  public Set<Agreement_info> getAgreements() {
    return agreements;
  }

  public void setAgreements(Set<Agreement_info> agreements) {
    this.agreements = agreements;
  }

  public Set<Agreement_item> getAgreementItems() {
    return agreementItems;
  }

  public void setAgreementItems(Set<Agreement_item> agreementItems) {
    this.agreementItems = agreementItems;
  }

  @Override
  public String toString() {
    return "Project_info{" +
        "name='" + name + '\'' +
        ", hot=" + hot +
        ", startTime=" + startTime +
        ", endTime=" + endTime +
        ", releaseTime=" + releaseTime +
        ", purpose='" + purpose + '\'' +
        ", status='" + status + '\'' +
        ", organizationInfo=" + organizationInfo +
        ", organization='" + organization + '\'' +
        ", projectItems=" + projectItems +
        ", agreements=" + agreements +
        ", agreementItems=" + agreementItems +
        '}';
  }
}


