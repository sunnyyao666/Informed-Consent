package cn.edu.fudan.biological;

import cn.edu.fudan.biological.domain.Organization_info;
import cn.edu.fudan.biological.domain.User_info;
import cn.edu.fudan.biological.repository.OrganizationInfoRepository;
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
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableJpaAuditing
public class BiologicalApplication {

    public static void main(String[] args) {
        SpringApplication.run(BiologicalApplication.class, args);
    }

    @Bean
    public CommandLineRunner dataLoader(UserInfoRepository userInfoRepository, OrganizationInfoRepository organizationInfoRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                init(userInfoRepository, organizationInfoRepository);
            }
        };
    }

    private void init(UserInfoRepository userInfoRepository, OrganizationInfoRepository organizationInfoRepository) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (userInfoRepository.findByUsername("13812345678") == null) {
            User_info userInfo = new User_info("13812345678", passwordEncoder.encode("123456"), "18302010017@fudan.edu.cn");
            userInfo.setSignature("123");
            userInfoRepository.save(userInfo);
            Organization_info organization_info = new Organization_info("Fudan University", "123456", "Zhang San", "310109000000000000");
            organization_info.setEmail("18302010017@fudan.edu.cn");
            organization_info.setPhone("13812345678");
            organizationInfoRepository.save(organization_info);
        }

    }

}
