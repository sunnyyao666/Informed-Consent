package cn.edu.fudan.biological.serviceImpl;

import org.springframework.stereotype.Service;

/**
 * @program: biological
 * @description: 异常
 * @author: Shen Zhengyu
 * @create: 2020-10-16 19:18
 **/
public class WXOpenFailException extends RuntimeException{
    WXOpenFailException(String message) {
        super(message);
    }

    WXOpenFailException(String message, Throwable cause) {
        super(message, cause);
    }
}
