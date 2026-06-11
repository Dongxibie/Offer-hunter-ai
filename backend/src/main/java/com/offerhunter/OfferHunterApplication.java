package com.offerhunter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.offerhunter.mapper")
public class OfferHunterApplication {

    public static void main(String[] args) {
        SpringApplication.run(OfferHunterApplication.class, args);
    }
}
