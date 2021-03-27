package cn.edu.fudan.biological.controller.organization;

import cn.edu.fudan.biological.domain.Organization_info;
import cn.edu.fudan.biological.dto.MyResponse;
import cn.edu.fudan.biological.repository.OrganizationInfoRepository;
import cn.edu.fudan.biological.util.JWTUtils;
import lombok.extern.slf4j.Slf4j;
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

    @Autowired
    public OrganizationAccountController(OrganizationInfoRepository organizationInfoRepository) {
        this.organizationInfoRepository = organizationInfoRepository;
    }

    @PostMapping(path = "/code")
    public MyResponse getCode(@RequestParam String organization, HttpServletResponse response, HttpServletRequest request) {
      Organization_info organization_info = organizationInfoRepository.findByOrganization(organization);
      if (organization_info != null) {
        return MyResponse.fail("用户名重复", 1101);
      } else {
        String code = "123456";
        jedis.set(organization, code);
        jedis.expire(organization, 300);
        //To do
        return MyResponse.success();
      }
    }
    @PostMapping(path = "/register")
    public MyResponse register(@RequestParam String organization, @RequestParam String password,@RequestParam String code, @RequestParam String applicantName, @RequestParam String applicantId, @RequestParam String phone, @RequestParam String email, HttpServletResponse response, HttpServletRequest request) {
        Organization_info organization_info = organizationInfoRepository.findByOrganization(organization);
        if (organization_info != null) {
            return MyResponse.fail("用户名重复", 1101);
        } else {
            if (null != jedis.get(organization) && jedis.get(organization).equals(code)){
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

    @PostMapping(path = "/companyLogin")
    public MyResponse login(@RequestParam String organization, @RequestParam String password, HttpServletResponse response, HttpServletRequest request) {
        Organization_info organization_info = organizationInfoRepository.findByOrganization(organization);
        if (organization_info == null) {
            return MyResponse.fail("用户名不存在", 1102);
        } else {
            if (!organization_info.getPassword().equals(password)) {
                return MyResponse.fail("密码错误", 1103);
            }
            log.info(organization + "登录成功");
            return MyResponse.success();
        }
    }

    //找回密码（发验证码）
    @PostMapping(path = "/passwordCode")
    public MyResponse passwordCode(@RequestParam String organization, HttpServletResponse response, HttpServletRequest request) {
        return MyResponse.success();
    }

    //找回密码（修改密码）//使用邮箱链接
    @PostMapping(path = "/password")
    public MyResponse password(@RequestParam String organization, @RequestParam String newPassword, HttpServletResponse response, HttpServletRequest request) {
        Organization_info organization_info = organizationInfoRepository.findByOrganization(organization);
        if (organization_info == null) {
            return MyResponse.fail("用户名不存在", 1102);
        } else {
            organization_info.setPassword(newPassword);
            organizationInfoRepository.save(organization_info);
            return MyResponse.success();
        }
    }

}
