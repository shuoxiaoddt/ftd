package com.xs.middle.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class FtdZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(FtdZuulApplication.class, args);
    }

}
