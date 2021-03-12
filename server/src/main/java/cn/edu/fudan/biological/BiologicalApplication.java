package cn.edu.fudan.biological;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BiologicalApplication {

    public static void main(String[] args) {
        SpringApplication.run(BiologicalApplication.class, args);
    }

}
