package cn.edu.fudan.biological.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @program: biological
 * @description: 单位信息
 * @author: Yao Hongtao
 * @create: 2021-03-13 13:51
 **/
@Entity
public class Organization_info extends BaseEntity {
    @Column(unique = true)
    private String organization; // 单位名

    private String password;
    private String applicantName;
    private String applicantId;
    private String phone;
    private String email;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "organizationInfo")
    private Set<Project_info> projects = new HashSet<>();

    public Organization_info() {
    }

    public Organization_info(String organization, String password, String applicantName, String applicantId) {
        this.organization = organization;
        this.password = password;
        this.applicantName = applicantName;
        this.applicantId = applicantId;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Project_info> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project_info> projects) {
        this.projects = projects;
    }
}

