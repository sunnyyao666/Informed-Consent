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
    private String username; // 手机号
    private String password;
    private String signature; // 手势密码对应数字序列

    @Column(unique = true)
    private String openId; // WeChat Openid

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER, mappedBy = "userInfo")
    private Set<User_star> stars = new HashSet<>();

    public User_info() {
    }

    public User_info(String openId, String username, String password) {
        this.openId = openId;
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Set<User_star> getStars() {
        return stars;
    }

    public void setStars(Set<User_star> stars) {
        this.stars = stars;
    }
}
