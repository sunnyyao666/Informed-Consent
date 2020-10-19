package cn.edu.fudan.biological.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * @program: biological
 * @description: 用户信息
 * @author: Shen Zhengyu
 * @create: 2020-10-16 19:05
 **/
@Entity
public class user_info {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer user_id;
    @Column(unique = true)
    private String open_id; // WeChat Openid

    private String tel_number;

    private String user_name;

    private String user_pwd;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date create_time;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date update_time;

    public Integer getUserId() {
        return user_id;
    }

    public void setUserId(Integer user_id) {
        this.user_id = user_id;
    }

    public String getOpenId() {
        return open_id;
    }

    public void setOpenId(String open_id) {
        this.open_id = open_id;
    }

    public String getTelNumber() {
        return tel_number;
    }

    public void setTelNumber(String tel_number) {
        this.tel_number = tel_number;
    }

    public String getUserName() {
        return user_name;
    }

    public void setUserName(String user_name) {
        this.user_name = user_name;
    }

    public String getUserPwd() {
        return user_pwd;
    }

    public void setUserPwd(String user_pwd) {
        this.user_pwd = user_pwd;
    }

    public Date getCreateTime() {
        return create_time;
    }

    public void setCreateTime(Date create_time) {
        this.create_time = create_time;
    }

    public Date getUpdateTime() {
        return update_time;
    }

    public void setUpdateTime(Date update_time) {
        this.update_time = update_time;
    }
}
