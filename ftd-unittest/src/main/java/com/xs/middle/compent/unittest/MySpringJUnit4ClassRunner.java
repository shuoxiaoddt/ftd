package com.xs.middle.compent.unittest;

import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.apache.logging.log4j.core.config.ConfigurationFactory.CONFIGURATION_FILE_PROPERTY;


public class MySpringJUnit4ClassRunner extends SpringJUnit4ClassRunner {


    public MySpringJUnit4ClassRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
        System.setProperty(ConfigurationFactory.CONFIGURATION_FILE_PROPERTY, "log4j2_appender.xml,log4j2.xml");
    }
}