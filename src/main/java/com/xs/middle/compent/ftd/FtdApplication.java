package com.xs.middle.compent.ftd;

import com.xs.middle.compent.ftd.autoconf.LinuxSystem;
import com.xs.middle.compent.ftd.autoconf.register.EnableSystemAutoRegisterAutoConfiguration;
import com.xs.middle.compent.ftd.autoconf.seletor.EnableSystemAutoConfiguration;
import com.xs.middle.compent.ftd.config.SystemConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class FtdApplication {

    public static void main(String[] args) {
        SystemConfig.setSystemConfiguration();
        ConfigurableApplicationContext context = SpringApplication.run(FtdApplication.class, args);
        LinuxSystem bean = context.getBean(LinuxSystem.class);
        System.out.println(bean);
    }

}
