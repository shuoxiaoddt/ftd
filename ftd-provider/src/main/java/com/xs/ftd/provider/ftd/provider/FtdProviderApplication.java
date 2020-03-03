package com.xs.ftd.provider.ftd.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages={"com.xs.ftd"})
public class FtdProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(FtdProviderApplication.class, args);
    }

}
