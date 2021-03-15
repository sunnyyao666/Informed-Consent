package cn.edu.fudan.biological.controller.request.user;

import cn.edu.fudan.biological.domain.Agreement_response;

import java.util.List;

/**
 * @program: biological
 * @description: 用户项目相关请求
 * @author: Yao Hongtao
 * @create: 2021-03-15 15:58
 **/
public class UserProjectRequest {
    String username;
    Integer pid;
    String signature;
    String method;
    List<Agreement_response> data;
    Integer offset;
    Integer sum;

    public UserProjectRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<Agreement_response> getData() {
        return data;
    }

    public void setData(List<Agreement_response> data) {
        this.data = data;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getSum() {
        return sum;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }
}

