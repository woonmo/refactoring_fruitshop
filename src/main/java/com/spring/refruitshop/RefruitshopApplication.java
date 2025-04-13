package com.spring.refruitshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing  // validation 위한 어노테이션
public class RefruitshopApplication {

    public static void main(String[] args) {
        SpringApplication.run(RefruitshopApplication.class, args);
    }

}
