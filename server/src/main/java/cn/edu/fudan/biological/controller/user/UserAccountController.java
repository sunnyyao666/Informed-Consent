package cn.edu.fudan.biological.controller.user;

import cn.edu.fudan.biological.controller.request.user.UserAccountRequest;
import cn.edu.fudan.biological.domain.User_info;
import cn.edu.fudan.biological.dto.MyResponse;
import cn.edu.fudan.biological.repository.UserInfoRepository;
import cn.edu.fudan.biological.util.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import redis.clients.jedis.Jedis;


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
    private final MailUtil mailUtil;

    @Autowired
    public UserAccountController(UserInfoRepository userInfoRepository, MailUtil mailUtil) {
        this.userInfoRepository = userInfoRepository;
        this.mailUtil = mailUtil;
    }

    @GetMapping("/test")
    public MyResponse test(@RequestParam String param) {
        jedis.set("1", "2");
        return MyResponse.success("4.15+ " + "成功", param + jedis.get("1"));
    }

    @GetMapping("/mail")
    public MyResponse testMail(@RequestParam String param) {
        try {
            mailUtil.sendCodeMail("1372439230@qq.com", param);
            return MyResponse.success();
        } catch (MailException e) {
            return MyResponse.fail(e.getMessage(), 1001);
        }
    }

    @GetMapping("/code")
    public MyResponse userRegisterGetCode(@RequestParam("username") String username, @RequestParam("mail") String email) {
        User_info userInfo = userInfoRepository.findByUsername(username);
        if (userInfo != null) {
            return MyResponse.fail("用户名重复", 1101);
        }

//        String code = Integer.toString((int) (Math.random() * 900000 + 100000));
//        try {
//            mailUtil.sendCodeMail(email, code);
//        } catch (MailException e) {
//            return MyResponse.fail("邮件发送失败", 1001);
//        }
        String code = "123456";
        jedis.set(username, code);
        jedis.expire(username, 300);

        return MyResponse.success();
    }

    @PostMapping("/register")
    public MyResponse userRegister(@RequestBody UserAccountRequest userAccountRequest) {
        String username = userAccountRequest.getUsername();
        User_info userInfo = userInfoRepository.findByUsername(username);
        if (userInfo != null) {
            return MyResponse.fail("用户名重复", 1101);
        }

        String code = userAccountRequest.getCode();
        String savedCode = jedis.get(username);
        if (savedCode == null || !savedCode.equals(code)) {
            return MyResponse.fail("验证码错误", 1102);
        }

        String password = userAccountRequest.getPassword();
        String email = userAccountRequest.getMail();
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

//        String code = Integer.toString((int) (Math.random() * 900000 + 100000));
//        try {
//            mailUtil.sendCodeMail(email, code);
//        } catch (MailException e) {
//            return MyResponse.fail("邮件发送失败", 1001);
//        }
        String code = "123456";
        jedis.set(username, code);
        jedis.expire(username, 300);

        return MyResponse.success();
    }

    @PostMapping("/forgetCode")
    public MyResponse forgetPasswordVerifyCode(@RequestBody UserAccountRequest userAccountRequest) {
        String username = userAccountRequest.getUsername();
        User_info userInfo = userInfoRepository.findByUsername(username);
        if (userInfo == null) {
            return MyResponse.fail("用户名不存在或验证码不正确", 1102);
        }

        String code = userAccountRequest.getCode();
        String savedCode = jedis.get(username);
        if (savedCode == null || !savedCode.equals(code)) {
            return MyResponse.fail("验证码错误", 1102);
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

//        String code = Integer.toString((int) (Math.random() * 900000 + 100000));
//        try {
//            mailUtil.sendCodeMail(email, code);
//        } catch (MailException e) {
//            return MyResponse.fail("邮件发送失败", 1001);
//        }
        String code = "123456";
        jedis.set(username, code);
        jedis.expire(username, 300);

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

        String code = userAccountRequest.getCode();
        String savedCode = jedis.get(username);
        if (savedCode == null || !savedCode.equals(code)) {
            return MyResponse.fail("验证码错误", 1102);
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

        userInfo.setSignature(stringBuilder.toString());
        userInfoRepository.save(userInfo);
        return MyResponse.success();
    }
}

