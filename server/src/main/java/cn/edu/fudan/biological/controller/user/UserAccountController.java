package cn.edu.fudan.biological.controller.user;

import cn.edu.fudan.biological.controller.request.user.UserAccountRequest;
import cn.edu.fudan.biological.domain.User_info;
import cn.edu.fudan.biological.dto.MyResponse;
import cn.edu.fudan.biological.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: biological
 * @description: 用户账户相关功能
 * @author: Yao Hongtao
 * @create: 2021-03-15 14:52
 **/
@RestController
@RequestMapping(value = "/api/user")
public class UserAccountController {
    private final UserInfoRepository userInfoRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final Jedis jedis = new Jedis("localhost");

    @Autowired
    public UserAccountController(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    //    验证码发送
    @GetMapping("/code")
    public MyResponse userRegisterGetCode(@RequestBody UserAccountRequest userAccountRequest) {
        String username = userAccountRequest.getUsername();
        User_info userInfo = userInfoRepository.findByUsername(username);
        if (userInfo != null) return MyResponse.fail("用户名重复", 1101);

        String code = "123456";
        jedis.set(username, code);
        jedis.expire(username, 300);

        return MyResponse.success();
    }

    //    验证码验证
    @PostMapping("/register")
    public MyResponse userRegister(@RequestBody UserAccountRequest userAccountRequest) {
        String username = userAccountRequest.getUsername();
        User_info userInfo = userInfoRepository.findByUsername(username);
        if (userInfo != null) return MyResponse.fail("用户名重复", 1101);

        String code = userAccountRequest.getCode();
        String redisCode = jedis.get(username);
        if (redisCode == null || !redisCode.equals(code)) return MyResponse.fail("验证码错误", -1);

        String password = userAccountRequest.getPassword();
        String email = userAccountRequest.getEmail();
        userInfo = new User_info(username, passwordEncoder.encode(password), email);
        userInfoRepository.save(userInfo);
        return MyResponse.success();
    }

    @PostMapping("/userLogin")
    public MyResponse userLogin(@RequestBody UserAccountRequest userAccountRequest) {
        String username = userAccountRequest.getUsername();
        User_info userInfo = userInfoRepository.findByUsername(username);
        if (userInfo == null) return MyResponse.fail("用户名不存在", 1102);

        String password = userAccountRequest.getPassword();
        if (!passwordEncoder.matches(password, userInfo.getPassword())) return MyResponse.fail("密码错误", 1103);

        return MyResponse.success();
    }

    @PostMapping("/signature")
    public MyResponse setSignature(@RequestBody UserAccountRequest userAccountRequest) {
        String username = userAccountRequest.getUsername();
        User_info userInfo = userInfoRepository.findByUsername(username);
        if (userInfo == null) return MyResponse.fail("用户名不存在", 1102);

        String password = userAccountRequest.getPassword();
        if (!passwordEncoder.matches(password, userInfo.getPassword())) return MyResponse.fail("密码错误", 1103);

        int[] gesture = userAccountRequest.getGesture();
        StringBuilder stringBuilder = new StringBuilder();
        for (int value : gesture) stringBuilder.append(value);
        String signature = stringBuilder.toString();

        userInfo.setSignature(signature);
        userInfoRepository.save(userInfo);
        return MyResponse.success();
    }

    @GetMapping("/hasSignature")
    public MyResponse hasSignature(@RequestBody UserAccountRequest userAccountRequest) {
        String username = userAccountRequest.getUsername();
        User_info userInfo = userInfoRepository.findByUsername(username);
        if (userInfo == null) return MyResponse.fail("用户名不存在", 1102);

        String password = userAccountRequest.getPassword();
        if (!passwordEncoder.matches(password, userInfo.getPassword())) return MyResponse.fail("密码错误", 1103);

        String signature = userInfo.getSignature();
        Map<String, Boolean> data = new HashMap<>();
        data.put("hasSignature", !(signature == null || "".equals(signature)));
        return MyResponse.success("成功", data);
    }

    @PostMapping("/uncheckedSignature")
    public MyResponse checkSignature(@RequestBody UserAccountRequest userAccountRequest) {
        String username = userAccountRequest.getUsername();
        User_info userInfo = userInfoRepository.findByUsername(username);
        if (userInfo == null) return MyResponse.fail("用户名不存在", 1102);

        String password = userAccountRequest.getPassword();
        if (!passwordEncoder.matches(password, userInfo.getPassword())) return MyResponse.fail("密码错误", 1103);

        int[] gesture = userAccountRequest.getGesture();
        StringBuilder stringBuilder = new StringBuilder();
        for (int value : gesture) stringBuilder.append(value);
        String signature = stringBuilder.toString();

        if (signature.equals(userInfo.getSignature())) return MyResponse.success();
        else return MyResponse.fail("手势密码错误", 1103);
    }

    //    验证码发送
    @GetMapping("/signatureCode")
    public MyResponse signatureGetCode(@RequestBody UserAccountRequest userAccountRequest) {
        String username = userAccountRequest.getUsername();
        User_info userInfo = userInfoRepository.findByUsername(username);
        if (userInfo == null) return MyResponse.fail("用户名不存在", 1102);

        String password = userAccountRequest.getPassword();
        if (!passwordEncoder.matches(password, userInfo.getPassword())) return MyResponse.fail("密码错误", 1103);

        return MyResponse.success();
    }

}

