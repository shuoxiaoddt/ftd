package com.xs.middle.compent.config;

import com.google.common.collect.Lists;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.util.List;

/**
 * 系统配置类
 */
public interface SystemConfig {

    String CONFIGURATION_FILE_PROPERTY = "log4j.configurationFile";

    /**
     * 设置系统
     */
    static void setSystemConfiguration() {
        //配置异步日志阻塞策略  @see org.apache.logging.log4j.core.async.AsyncQueueFullPolicyFactory
        System.setProperty("log4j2.AsyncQueueFullPolicy","Discard");
        System.setProperty("log4j2.DiscardThreshold","ERROR");
        System.setProperty(CONFIGURATION_FILE_PROPERTY, "log4j2_appender.xml,log4j2.xml");
    }


    /**
     * 获取类路径下log4j2配置文件
     *
     * @return
     */
    static List<Resource> getLogFile(String locationPattern) {
        List<Resource> files = Lists.newArrayList();
        try {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources(locationPattern);
            for (Resource r : resources) {
                files.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return files;
    }
}
