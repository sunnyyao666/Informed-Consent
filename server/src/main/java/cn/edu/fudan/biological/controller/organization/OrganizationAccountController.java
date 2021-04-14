package cn.edu.fudan.biological.controller.organization;

import java.util.Map;

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

  @PostMapping(path = "/forgetCode")
  public MyResponse getCode(@RequestBody Map<String,String> map)  {
    Organization_info organization_info = organizationInfoRepository.findByOrganization(map.get("unitname"));
    if (null != map.get("code")) {
      if (jedis.get( map.get("unitname")).equals( map.get("code"))) {
        //验证成功
        return MyResponse.success();
      } else {
        //验证失败
        return MyResponse.success();

      }
    } else {
      if (organization_info == null) {
        return MyResponse.fail("用户名不存在", 1101);
      } else {
        String yzcode = "123456";
        jedis.set(map.get("unitname"), yzcode);
        jedis.expire(map.get("unitname"), 300);
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
