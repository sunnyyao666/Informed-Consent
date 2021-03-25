package cn.edu.fudan.biological.controller.request.user;

/**
 * @program: biological
 * @description: 用户账户相关请求参数
 * @author: Yao Hongtao
 * @create: 2021-03-15 15:02
 **/
public class UserAccountRequest {
    private String username;
    private String password;
    private String code;
    private int[] gesture;
    private String email;

    public UserAccountRequest() {
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int[] getGesture() {
        return gesture;
    }

    public void setGesture(int[] gesture) {
        this.gesture = gesture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

