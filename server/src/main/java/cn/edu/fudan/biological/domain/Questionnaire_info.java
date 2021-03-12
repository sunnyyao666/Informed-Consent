package cn.edu.fudan.biological.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @program: biological
 * @description: 问卷信息
 * @author: Yao Hongtao
 * @create: 2021-03-12 20:42
 **/
@Entity
public class Questionnaire_info extends BaseEntity {
    private Integer personsNum = 0;
    private Integer starsNum = 0;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    private String purpose;
    private String extraInfo;

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER, mappedBy = "questionnaireInfo")
    private Set<User_signature> signatures = new HashSet<>();

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER, mappedBy = "questionnaireInfo")
    private Set<User_star> stars = new HashSet<>();


    public Questionnaire_info() {
    }

    public Questionnaire_info(Date startTime, Date endTime, String purpose, String extraInfo) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.purpose = purpose;
        this.extraInfo = extraInfo;
    }

    public Integer getPersonsNum() {
        return personsNum;
    }

    public void setPersonsNum(Integer personsNum) {
        this.personsNum = personsNum;
    }

    public Integer getStarsNum() {
        return starsNum;
    }

    public void setStarsNum(Integer starsNum) {
        this.starsNum = starsNum;
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

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public Set<User_signature> getSignatures() {
        return signatures;
    }

    public void setSignatures(Set<User_signature> signatures) {
        this.signatures = signatures;
    }

    public Set<User_star> getStars() {
        return stars;
    }

    public void setStars(Set<User_star> stars) {
        this.stars = stars;
    }
}


