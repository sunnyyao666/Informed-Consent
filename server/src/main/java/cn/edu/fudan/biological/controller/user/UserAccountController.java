package cn.edu.fudan.biological.controller.user;

import cn.edu.fudan.biological.controller.request.user.UserAccountRequest;
import cn.edu.fudan.biological.domain.User_info;
import cn.edu.fudan.biological.dto.MyResponse;
import cn.edu.fudan.biological.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Autowired
    public UserAccountController(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    //    微信openid ?
    @PostMapping("/register")
    public MyResponse userRegister(@RequestBody UserAccountRequest userAccountRequest) {
        String username = userAccountRequest.getUsername();
        User_info userInfo = userInfoRepository.findByUsername(username);
        if (userInfo != null) return MyResponse.fail("用户名重复", 1101);

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

    //    验证码
    @PostMapping("/signature")
    public MyResponse setSignature(@RequestBody UserAccountRequest userAccountRequest) {
        String username = userAccountRequest.getUsername();
        User_info userInfo = userInfoRepository.findByUsername(username);
        if (userInfo == null) return MyResponse.fail("用户名不存在", 1102);

        String password = userAccountRequest.getPassword();
        if (!passwordEncoder.matches(password, userInfo.getPassword())) return MyResponse.fail("密码错误", 1103);

        String signature = userAccountRequest.getSignature();
        if (signature == null) return MyResponse.success();

        userInfo.setSignature(signature);
        userInfoRepository.save(userInfo);
        return MyResponse.success();
    }

    //    验证码发送
    @GetMapping("/signatureCode")
    public MyResponse sendSignatureCode() {
        return MyResponse.success();
    }
}

