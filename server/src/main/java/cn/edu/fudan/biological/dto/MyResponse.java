package cn.edu.fudan.biological.dto;

/**
 * @program: biological
 * @description: 包装响应类
 * @author: Shen Zhengyu
 * @create: 2020-10-16 19:01
 **/

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyResponse implements Serializable {
    private int statusCode = 0;
    private String status_msg = "";
    private Object data;

    public static MyResponse success(String msg, Object o) {
        return new MyResponse(0, msg, o);
    }

    public static MyResponse success(String msg) {
        return MyResponse.success(msg, null);
    }

    public static MyResponse success() {
        return MyResponse.success("成功", null);
    }

    public static MyResponse fail(String msg) {
        return new MyResponse(-1, msg, null);
    }

    public static MyResponse fail(String msg, Object o) {
        return new MyResponse(-1, msg, o);
    }

    public static MyResponse fail(String msg, int errorCode) {
        return new MyResponse(errorCode, msg, null);
    }

    public static MyResponse fail(String msg, int errorCode, Object o) {
        return new MyResponse(errorCode, msg, o);
    }

    public static MyResponse tokenError() {
        return new MyResponse(0, "Token illegal", null);
    }
}
