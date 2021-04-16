package cn.edu.fudan.biological.controller.organization;

import java.util.Map;

import cn.edu.fudan.biological.domain.Organization_info;
import cn.edu.fudan.biological.dto.MyResponse;
import cn.edu.fudan.biological.repository.OrganizationInfoRepository;
import cn.edu.fudan.biological.util.JWTUtils;
import cn.edu.fudan.biological.util.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import redis.clients.jedis.Jedis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: biological
 * @description: 处理单位账户相关请求
 * @author: Shen Zhengyu
 * @create: 2021-03-14 17:38
 **/
@Slf4j
@RestController
@RequestMapping(value = "/api/unit")
public class OrganizationAccountController {

  private final OrganizationInfoRepository organizationInfoRepository;

  private final Jedis jedis = new Jedis("localhost");
    private final MailUtil mailUtil;
  @Autowired
  public OrganizationAccountController(OrganizationInfoRepository organizationInfoRepository, MailUtil mailUtil) {
    this.organizationInfoRepository = organizationInfoRepository;
    this.mailUtil = mailUtil;
  }

  @RequestMapping (path = "/forgetCode",method = RequestMethod.GET)
  public MyResponse getCode(@RequestParam("unitname") String unitname,@RequestParam("code") String code)  {
    Organization_info organization_info = organizationInfoRepository.findByOrganization(unitname);
    if (null == organization_info){
        return MyResponse.fail("单位不存在");
    }
    String email = organization_info.getEmail();
    if (null != code && !"".equals(code)) {
      if (jedis.get( unitname).equals( code)) {
        //验证成功,发送旧密码
        try {
            mailUtil.sendPasswordMail(email, organization_info.getPassword());
        } catch (MailException e) {
            return MyResponse.fail("邮件发送失败", 1001);
        }
        jedis.del(unitname);
        return MyResponse.success();
      } else {
        //验证失败
        return MyResponse.success();

      }
    } else {
      if (organization_info == null) {
        return MyResponse.fail("用户名不存在", 1101);
      } else {
                  String yzcode = Integer.toString((int) (Math.random() * 900000 + 100000));
        try {
            mailUtil.sendCodeMail(email, code);
        } catch (MailException e) {
            return MyResponse.fail("邮件发送失败", 1001);
        }
        jedis.set(unitname, yzcode);
        jedis.expire(unitname, 300);
        //To do
        return MyResponse.success();
      }
    }
  }

    @RequestMapping (path = "/forgetCode",method = RequestMethod.POST)
    public MyResponse verifyCode(@RequestBody Map<String,String> map)  {
      String unitname = map.get("unitname");
      String code = map.get("code");
        Organization_info organization_info = organizationInfoRepository.findByOrganization(unitname);
        if (null == organization_info){
            return MyResponse.fail("单位不存在");
        }
        String trueCode = jedis.get(unitname);
        if (null == trueCode || "".equals(trueCode)){
            return MyResponse.fail("没有验证码");
        }
        String email = organization_info.getEmail();
        if (null != map.get("code") && !"".equals(code)) {
            if (jedis.get( unitname).equals( code)) {
                //验证成功,发送旧密码
                try {
                    mailUtil.sendPasswordMail(email, organization_info.getPassword());
                } catch (MailException e) {
                    return MyResponse.fail("邮件发送失败", 1001);
                }
                jedis.del(unitname);
                return MyResponse.success();
            } else {
                //验证失败
                return MyResponse.success();

            }
        } else {
            if (organization_info == null) {
                return MyResponse.fail("用户名不存在", 1101);
            } else {
                String yzcode = Integer.toString((int) (Math.random() * 900000 + 100000));
                try {
                    mailUtil.sendCodeMail(email, code);
                } catch (MailException e) {
                    return MyResponse.fail("邮件发送失败", 1001);
                }
                jedis.set(unitname, yzcode);
                jedis.expire(unitname, 300);
                //To do
                return MyResponse.success();
            }
        }
    }

    @PostMapping(path = "/register")
    public MyResponse register(@RequestBody Map<String,String> map) {
      String organization = map.get("organization");
      Organization_info organization_info = organizationInfoRepository.findByOrganization(organization);
      String password = map.get("password");
      String code = map.get("code");
      String applicantName = map.get("applicantName");
      String applicantId = map.get("applicantId");
      String phone = map.get("phone");
      String email = map.get("email");

      if (organization_info != null) {
            return MyResponse.fail("用户名重复", 1101);
        } else {
//            if (null != jedis.get(organization) && jedis.get(organization).equals(code)){
              if(true){
              Organization_info new_organization_info = new Organization_info(organization, password, applicantName, applicantId);
              new_organization_info.setEmail(email);
              new_organization_info.setPhone(phone);
              organizationInfoRepository.save(new_organization_info);
              log.info(organization + "注册成功");
              return MyResponse.success();
            }else{
              return MyResponse.fail("验证码错误",1001);
            }

        }
    }

      @PostMapping(path = "/login")
    public MyResponse login(@RequestBody Map<String,String> map ){
      String unitname = map.get("unitname");

      String password = map.get("password");

      Organization_info organization_info = organizationInfoRepository.findByOrganization(unitname);
        if (organization_info == null) {
            return MyResponse.fail("用户名并不存在", 1102);
        } else {
            if (!organization_info.getPassword().equals(password)) {
                return MyResponse.fail("密码错误", 1103);
            }
            log.info(unitname + "登录成功");
            return MyResponse.success();
        }
    }

    //找回密码（发验证码）
    @PostMapping(path = "/passwordCode")
    public MyResponse passwordCode(@RequestBody Map<String,String> map ) {
        return MyResponse.success();
    }

    //找回密码（修改密码）//使用邮箱链接
    @PostMapping(path = "/newPassword")
    public MyResponse password(@RequestBody Map<String,String> map) {
      String unitname = map.get("unitname");

      String password = map.get("password");
        Organization_info organization_info = organizationInfoRepository.findByOrganization(unitname);
        if (organization_info == null) {
            return MyResponse.fail("用户名不存在", 1102);
        } else {
            organization_info.setPassword(password);
            organizationInfoRepository.save(organization_info);
            return MyResponse.success();
        }
    }

}
