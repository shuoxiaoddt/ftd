package com.xs.middle.compent.aop;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.xs.middle.compent.aop.AopLog.REQUEST_ID;


/**
 * 请求id设置
 */
@Component
@Order(1)
@Aspect
public class RequestAspect {

    @Pointcut("execution(public * com.xs..*Controller.*(..)) ")
    public void request() {
    }


    @Before("request()")
    public void before() {
        String requestId = MDC.get(REQUEST_ID);
        if (StringUtils.isBlank(requestId)) {
            MDC.put(REQUEST_ID, UUID.randomUUID().toString());
        }
    }

    @After("request()")
    public void after(){
        MDC.remove(REQUEST_ID);
    }
}
