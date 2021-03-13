package cn.edu.fudan.biological.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @program: biological
 * @description: 用户信息
 * @author: Shen Zhengyu
 * @create: 2020-10-16 19:05
 **/
@Entity
public class User_info extends BaseEntity {
    @Column(unique = true)
    private String openId; // WeChat Openid

    private String telNumber;

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER, mappedBy = "userInfo")
    private Set<User_signature> signatures = new HashSet<>();

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER, mappedBy = "userInfo")
    private Set<User_star> stars = new HashSet<>();

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER, mappedBy = "userInfo")
    private Set<Questionnaire_record> records = new HashSet<>();

    public User_info() {
    }

    public User_info(String openId, String telNumber) {
        this.openId = openId;
        this.telNumber = telNumber;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
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

    public Set<Questionnaire_record> getRecords() {
        return records;
    }

    public void setRecords(Set<Questionnaire_record> records) {
        this.records = records;
    }
}
