package com.xs.middle.compent.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author xiaos
 * @date 2019/12/6 10:53
 */
@Component
public class LogBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.err.println("BeforeInitialization:bean-->"+bean+"  beanName-->"+beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.err.println("AfterInitialization:bean-->"+bean+"  beanName-->"+beanName);
        return bean;
    }
}
