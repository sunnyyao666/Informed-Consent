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

    @GetMapping("/test")
    public MyResponse test(@RequestParam String param) {
        return MyResponse.success("成功", param + userInfoRepository.findByUsername("13812345678").getEmail());
    }

    @GetMapping("/code")
    public MyResponse userRegisterGetCode(@RequestParam("username") String username) {
        User_info userInfo = userInfoRepository.findByUsername(username);
        if (userInfo != null) {
            return MyResponse.fail("用户名重复", 1101);
        }

        return MyResponse.success();
    }

    @PostMapping("/register")
    public MyResponse userRegister(@RequestBody UserAccountRequest userAccountRequest) {
        String username = userAccountRequest.getUsername();
        User_info userInfo = userInfoRepository.findByUsername(username);
        if (userInfo != null) {
            return MyResponse.fail("用户名重复", 1101);
        }

        String password = userAccountRequest.getPassword();
        String email = userAccountRequest.getEmail();
        userInfo = new User_info(username, passwordEncoder.encode(password), email);
        userInfoRepository.save(userInfo);
        return MyResponse.success();
    }

    @PostMapping("/login")
    public MyResponse userLogin(@RequestBody UserAccountRequest userAccountRequest) {
        String username = userAccountRequest.getUsername();
        User_info userInfo = userInfoRepository.findByUsername(username);
        if (userInfo == null) {
            return MyResponse.fail("用户名不存在或密码错误", 1102);
        }

        String password = userAccountRequest.getPassword();
        if (!passwordEncoder.matches(password, userInfo.getPassword())) {
            return MyResponse.fail("用户名不存在或密码错误", 1102);
        }

        return MyResponse.success();
    }

    @GetMapping("/forgetCode")
    public MyResponse forgetPasswordGetCode(@RequestParam("username") String username) {
        User_info userInfo = userInfoRepository.findByUsername(username);
        if (userInfo == null) {
            return MyResponse.fail("用户名不存在", 1102);
        }

        return MyResponse.success();
    }

    @PostMapping("/forgetCode")
    public MyResponse forgetPasswordVerifyCode(@RequestBody UserAccountRequest userAccountRequest) {
        String username = userAccountRequest.getUsername();
        User_info userInfo = userInfoRepository.findByUsername(username);
        if (userInfo == null) {
            return MyResponse.fail("用户名不存在或验证码不正确", 1102);
        }

        return MyResponse.success();
    }

    @PostMapping("/newPassword")
    public MyResponse setNewPassword(@RequestBody UserAccountRequest userAccountRequest) {
        String username = userAccountRequest.getUsername();
        User_info userInfo = userInfoRepository.findByUsername(username);
        if (userInfo == null) {
            return MyResponse.fail("出错", 1102);
        }

        String password = userAccountRequest.getPassword();
        userInfo.setPassword(passwordEncoder.encode(password));
        userInfoRepository.save(userInfo);
        return MyResponse.success();
    }

    @PutMapping("/signature")
    public MyResponse setSignature(@RequestBody UserAccountRequest userAccountRequest) {
        String username = userAccountRequest.getUsername();
        User_info userInfo = userInfoRepository.findByUsername(username);
        if (userInfo == null) {
            return MyResponse.fail("用户名不存在或密码错误", 1102);
        }

        String password = userAccountRequest.getPassword();
        if (!passwordEncoder.matches(password, userInfo.getPassword())) {
            return MyResponse.fail("用户名不存在或密码错误", 1102);
        }

        int[] gesture = userAccountRequest.getGesture();
        StringBuilder stringBuilder = new StringBuilder();
        for (int value : gesture) {
            stringBuilder.append(value);
        }
        String signature = stringBuilder.toString();
        userInfo.setSignature(signature);

        userInfoRepository.save(userInfo);
        return MyResponse.success();
    }

    @PostMapping("/signature")
    public MyResponse verifySignature(@RequestBody UserAccountRequest userAccountRequest) {
        String username = userAccountRequest.getUsername();
        User_info userInfo = userInfoRepository.findByUsername(username);
        if (userInfo == null) {
            return MyResponse.fail("用户名不存在或密码错误", 1102);
        }

        String password = userAccountRequest.getPassword();
        if (!passwordEncoder.matches(password, userInfo.getPassword())) {
            return MyResponse.fail("用户名不存在或密码错误", 1102);
        }

        int[] gesture = userAccountRequest.getGesture();
        StringBuilder stringBuilder = new StringBuilder();
        for (int value : gesture) {
            stringBuilder.append(value);
        }
        String signature = stringBuilder.toString();
        if (!signature.equals(userInfo.getSignature())) {
            return MyResponse.fail("用户名不存在或密码错误", 1102);
        }

        return MyResponse.success();
    }

    @GetMapping("/gestureCode")
    public MyResponse getGestureCode(@RequestParam("username") String username, @RequestParam("password") String password) {
        User_info userInfo = userInfoRepository.findByUsername(username);
        if (userInfo == null) {
            return MyResponse.fail("用户名不存在或密码错误", 1102);
        }

        if (!passwordEncoder.matches(password, userInfo.getPassword())) {
            return MyResponse.fail("用户名不存在或密码错误", 1102);
        }

        return MyResponse.success();
    }

    @PostMapping("/gestureCode")
    public MyResponse verifyGestureCode(@RequestBody UserAccountRequest userAccountRequest) {
        String username = userAccountRequest.getUsername();
        User_info userInfo = userInfoRepository.findByUsername(username);
        if (userInfo == null) {
            return MyResponse.fail("用户名不存在或密码错误", 1102);
        }

        String password = userAccountRequest.getPassword();
        if (!passwordEncoder.matches(password, userInfo.getPassword())) {
            return MyResponse.fail("用户名不存在或密码错误", 1102);
        }

        return MyResponse.success();
    }

    @PutMapping("/forgetSignature")
    public MyResponse newSignature(@RequestBody UserAccountRequest userAccountRequest) {
        String username = userAccountRequest.getUsername();
        User_info userInfo = userInfoRepository.findByUsername(username);
        if (userInfo == null) {
            return MyResponse.fail("用户名不存在或密码错误", 1102);
        }

        String password = userAccountRequest.getPassword();
        if (!passwordEncoder.matches(password, userInfo.getPassword())) {
            return MyResponse.fail("用户名不存在或密码错误", 1102);
        }

        int[] gesture = userAccountRequest.getGesture();
        StringBuilder stringBuilder = new StringBuilder();
        for (int value : gesture) {
            stringBuilder.append(value);
        }
        String signature = stringBuilder.toString();
        userInfo.setSignature(signature);

        userInfoRepository.save(userInfo);
        return MyResponse.success();
    }
}

