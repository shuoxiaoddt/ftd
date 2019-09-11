package com.xs.middle.compent.ftd;

import com.xs.middle.compent.ftd.config.SystemConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FtdApplication {

    public static void main(String[] args) {
        SystemConfig.setSystemConfiguration();
        SpringApplication.run(FtdApplication.class, args);
    }

}
