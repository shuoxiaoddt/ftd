package com.xs.middle.feign.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication
@EnableFeignClients
@ComponentScan(basePackages={"com.xs.middle"})
public class FtdFeignApplication {

    public static void main(String[] args) {
        SpringApplication.run(FtdFeignApplication.class, args);
    }

}
