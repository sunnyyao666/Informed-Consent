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
@Data
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


}
