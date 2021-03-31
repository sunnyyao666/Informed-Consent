package cn.edu.fudan.biological;

import cn.edu.fudan.biological.domain.User_info;
import cn.edu.fudan.biological.repository.UserInfoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableJpaAuditing
public class BiologicalApplication {

    public static void main(String[] args) {
        SpringApplication.run(BiologicalApplication.class, args);
    }

    @Bean
    public CommandLineRunner dataLoader(UserInfoRepository userInfoRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                init(userInfoRepository);
            }
        };
    }

    private void init(UserInfoRepository userInfoRepository) {
//        userInfoRepository.save(new User_info("root","123456","18302010017@fudan.edu.cn"));
        System.out.println(userInfoRepository.findByUsername("root"));
    }

}
