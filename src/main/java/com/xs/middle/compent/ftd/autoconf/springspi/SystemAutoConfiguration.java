package com.xs.middle.compent.ftd.autoconf.springspi;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.sql.DataSource;

/**
 * @author 39466
 * @date 2019/9/15 19:47
 * 通过Spring 提供的spi自动注入bean
 * @SEE org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration
 */
@org.springframework.context.annotation.Configuration
public class SystemAutoConfiguration {


}
