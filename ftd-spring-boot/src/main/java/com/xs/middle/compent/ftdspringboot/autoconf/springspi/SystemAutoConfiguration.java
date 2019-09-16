package com.xs.middle.compent.ftdspringboot.autoconf.springspi;

import com.xs.middle.compent.ftdspringboot.autoconf.LinuxSystem;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author 39466
 * @date 2019/9/15 19:47
 * 通过Spring 提供的spi自动注入bean
 * @SEE org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration
 */
@org.springframework.context.annotation.Configuration
public class SystemAutoConfiguration {

    @Bean
    public LinuxSystem linuxSystem(){
        return new LinuxSystem();
    }

}
