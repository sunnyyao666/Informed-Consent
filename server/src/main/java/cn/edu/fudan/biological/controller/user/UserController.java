package cn.edu.fudan.biological.controller.user;

import cn.edu.fudan.biological.config.AppProperties;
import cn.edu.fudan.biological.domain.user_info;
import cn.edu.fudan.biological.dto.MyResponse;
import cn.edu.fudan.biological.repository.UserInfoRepository;
import cn.edu.fudan.biological.service.WXOpenService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * @program: biological
 * @description: 用户相关路由
 * @author: Shen Zhengyu
 * @create: 2020-10-16 19:21
 **/
@RestController
@Slf4j
@RequestMapping(path = "/user")
@CrossOrigin
public class UserController {
    private final AppProperties appProperties;
    private final UserInfoRepository userInfoRepository;
    private final WXOpenService wxOpenService;

    @Autowired
    public UserController(UserInfoRepository UserInfoRepository, AppProperties appProperties, WXOpenService wxOpenService) {
        this.userInfoRepository = UserInfoRepository;
        this.appProperties = appProperties;
        this.wxOpenService = wxOpenService;
    }

    @GetMapping(path = "/test")
    public MyResponse test() {
        return MyResponse.success("Connect successfully.");
    }

    @GetMapping(path = "/login")
    public MyResponse login(@RequestParam final String code) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            final String baseUrl = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appProperties.getAppId() + "&secret=" + appProperties.getAppSecret() + "&js_code=" + code + "&grant_type=authorization_code";
            URI uri = new URI(baseUrl);

            ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseEntity.getBody());

            if (root.path("errcode").asInt() == 0) {
                // get open_id successfully.
                String openId = root.path("openid").asText();
                user_info user = userInfoRepository.findTopByOpenId(openId);
                if (user == null) {
                    user = new user_info();
                    user.setOpenId(openId);
                    userInfoRepository.save(user);
                    log.info("Add a new user: " + user);
                }
                return MyResponse.success("登陆成功", responseEntity.getBody());
            } else {
                // get openid failed
                return MyResponse.fail("登陆失败", root.path("errmsg").asText());
            }
        } catch (Exception e) {
            return MyResponse.fail("登陆失败", e);
        }

    }


    @GetMapping(path = "/loginBetter")
    public MyResponse login2(@RequestParam final String code) {
        String openid = wxOpenService.getOpenId(code);
        user_info user = userInfoRepository.findTopByOpenId(openid);
        if (user == null) {
            user = new user_info();
            user.setOpenId(openid);
            userInfoRepository.save(user);
            log.info("Add a new user: " + user);
        }
        return MyResponse.success("登陆成功", openid);
    }

}
