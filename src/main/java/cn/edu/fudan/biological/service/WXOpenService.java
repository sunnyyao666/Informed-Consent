package cn.edu.fudan.biological.service;

import org.springframework.stereotype.Service;

public interface WXOpenService {
    String getAccessToken();

    String getOpenId(String code);

    byte[] getWXACode(String scene, String page);
}
