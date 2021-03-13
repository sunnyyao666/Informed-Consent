package cn.edu.fudan.biological.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

/**
 * @program: biological
 * @description: 组织信息
 * @author: Yao Hongtao
 * @create: 2021-03-13 13:51
 **/
@Entity
public class Organization_info extends BaseEntity {
    private String organizationName;
    private String applicantName;
    private String applicantId;

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER, mappedBy = "organizationInfo")
    private Set<Questionnaire_info> questionnaires = new HashSet<>();

    public Organization_info() {
    }

    public Organization_info(String organizationName, String applicantName, String applicantId) {
        this.organizationName = organizationName;
        this.applicantName = applicantName;
        this.applicantId = applicantId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
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

    public Set<Questionnaire_info> getQuestionnaires() {
        return questionnaires;
    }

    public void setQuestionnaires(Set<Questionnaire_info> questionnaires) {
        this.questionnaires = questionnaires;
    }
}

