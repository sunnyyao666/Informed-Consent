package cn.edu.fudan.biological;

import java.util.HashMap;
import java.util.HashSet;

import cn.edu.fudan.biological.controller.organization.OrganizationAccountController;
import cn.edu.fudan.biological.controller.organization.OrganizationProjectController;
import cn.edu.fudan.biological.controller.request.unit.SaveProjectDraftRequest;
import cn.edu.fudan.biological.domain.Organization_info;
import cn.edu.fudan.biological.domain.Project_info;
import cn.edu.fudan.biological.domain.User_info;
import cn.edu.fudan.biological.repository.AgreementInfoRepository;
import cn.edu.fudan.biological.repository.AgreementItemRepository;
import cn.edu.fudan.biological.repository.AgreementResponseRepository;
import cn.edu.fudan.biological.repository.OrganizationInfoRepository;
import cn.edu.fudan.biological.repository.ProjectInfoRepository;
import cn.edu.fudan.biological.repository.ProjectItemRepository;
import cn.edu.fudan.biological.repository.UserInfoRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @program: biological
 * @description: 项目入口
 * @author: Yao Hongtao
 * @create: 2020-10-16 19:21
 **/
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableJpaAuditing
public class BiologicalApplication {

  public static void main(String[] args) {
    SpringApplication.run(BiologicalApplication.class, args);
  }

  @Bean
  public CommandLineRunner dataLoader(AgreementInfoRepository agreementInfoRepository,
      AgreementItemRepository agreementItemRepository, AgreementResponseRepository agreementResponseRepository,
      ProjectInfoRepository projectInfoRepository, ProjectItemRepository projectItemRepository,
      UserInfoRepository userInfoRepository, OrganizationInfoRepository organizationInfoRepository,
      OrganizationProjectController organizationProjectController, OrganizationAccountController organizationAccountController) {
    return new CommandLineRunner() {
      @Override
      public void run(String... args) throws Exception {
        init(userInfoRepository, organizationInfoRepository);
        HashSet<HashMap<String, Object>> agreeitems = new HashSet<>();
        HashMap<String, Object> tmp1 = new HashMap<>();
        tmp1.put("iid", 1);
        tmp1.put("name", "always");
        tmp1.put("value", "总是");
        tmp1.put("description", "test always");
        agreeitems.add(tmp1);
        HashMap<String, Object> tmp2 = new HashMap<>();
        tmp2.put("iid", 2);
        tmp2.put("name", "always2");
        tmp2.put("value", "总是2");
        tmp2.put("description", "test always2");
        agreeitems.add(tmp2);
        HashSet<HashMap<String, Object>> pjitems = new HashSet<>();
        HashMap<String, Object> tmp3 = new HashMap<>();
        HashMap<String, Object> tmp4 = new HashMap<>();
        tmp3.put("aid",1);
        tmp3.put("name","voice");
        tmp3.put("description","desvoice");
        tmp4.put("aid",2);
        tmp4.put("name","sight");
        tmp4.put("description","dessight");
        pjitems.add(tmp3);
        pjitems.add(tmp4);
        SaveProjectDraftRequest s = new SaveProjectDraftRequest("testUnit", null, "testProject", "To test robutness",
            "2017.01.01-2020.03.03",pjitems,agreeitems,true );
        organizationProjectController.publishProject(s);
        pjitems.clear();
        tmp3.put("name","height");
        pjitems.add(tmp3);
        SaveProjectDraftRequest s2 = new SaveProjectDraftRequest("testUnit2", null, "testProject2", "To test robutness",
            "2033.01.01-2044.03.03",pjitems,agreeitems,false );

        Organization_info oi = new Organization_info("testUnit","123456","administrator","310222222222222");
        organizationInfoRepository.save(oi);

        organizationProjectController.saveProjectDraft(s2);
        HashMap<String,String> unit1 = new HashMap<>();
        unit1.put("organization","testUnit");
        unit1.put("password","123456");
        unit1.put("code","7878");
        unit1.put("applicantName","administrator");
        unit1.put("applicantId","310222222222222");
        unit1.put("phone","123123832832");
        unit1.put("email","111@qq.com");
        organizationAccountController.register(unit1);
        HashMap<String,String> unit2 = new HashMap<>();
        unit2.put("organization","testUnit2");
        unit2.put("password","123456");
        unit2.put("code","7878");
        unit2.put("applicantName","administrator");
        unit2.put("applicantId","310222222222222");
        unit2.put("phone","123123832832");
        unit2.put("email","111@qq.com");
        organizationAccountController.register(unit2);
        HashMap<String,String> unit3 = new HashMap<>();
        unit3.put("organization","autoGeneratedUnitpassword123456");
        unit3.put("password","123456");
        unit3.put("code","7878");
        unit3.put("applicantName","administrator");
        unit3.put("applicantId","310222222222222");
        unit3.put("phone","123123832832");
        unit3.put("email","111@qq.com");
        organizationAccountController.register(unit3);

      }
    };
  }

  private void init(UserInfoRepository userInfoRepository, OrganizationInfoRepository organizationInfoRepository) {
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    if (userInfoRepository.findByUsername("13812345678") == null) {
      User_info userInfo = new User_info("13812345678", passwordEncoder.encode("123456"), "18302010017@fudan.edu.cn");
      userInfo.setSignature("123");
      userInfoRepository.save(userInfo);
      Organization_info organization_info = new Organization_info("Fudan University", "123456", "Zhang San",
          "310109000000000000");
      organization_info.setEmail("18302010017@fudan.edu.cn");
      organization_info.setPhone("13812345678");
      organizationInfoRepository.save(organization_info);
    }

  }

}
