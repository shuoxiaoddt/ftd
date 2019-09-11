package com.xs.middle.compent.ftd.aop;

import com.alibaba.fastjson.JSONObject;
import com.xs.middle.compent.ftd.util.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

/**
 * 日志记录
 * 1、所有请求信息和返回信息
 * 2、异常信息
 *
 */
@Component
@Aspect
@Slf4j
@Order(20)
public class AopLog {
    /**
     * 提示信息格式
     */
    private final static String MSG = "\nIP:{}\nURL:{} Consuming:{}ms\nHTTP_METHOD:{}\nCLASS_METHOD:{}\nOperator:{}\nARGS:{}\nResult:{}";

    public static final String REQUEST_ID = "REQUEST_ID";

    public static final String NAME_UTIL = "util";

    private static final Logger logger = LoggerFactory.getLogger("requestTime");

    private static final Logger bigLogger = LoggerFactory.getLogger("bigRequestTime");

    private static final Logger mongodbLogger = LoggerFactory.getLogger("mongodbLog");

    @Value("${arg.limit:2048}")
    private int limit;

    @Value("${return.length.limit:10240}")
    private int returnLimit;

    @Value("${request.alter.time:30000}")
    private long requestAlertTime;

    /**
     * be
     * 切入点，包括所有的 API-Service，WS-Service，Service 和 controller
     */
    @Pointcut("execution(public * com.xs..*ServiceImpl.*(..)) || execution(public * com.xs..*Controller.*(..))")
    public void log() {
    }


    /**
     * 进行日志记录
     *
     * @param joinPoint 切入信息
     * @return 实际方法的返回信息
     * @throws Throwable 实际方法抛出的异常-拦截后进行记录，并继续往外抛
     */
    @Around("log()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger defaultLogger = logger;
        long startTime = System.currentTimeMillis();
        Object[] args = joinPoint.getArgs();
        String argsStr = Arrays.toString(args);
        if(argsStr.length() >= limit){
//            defaultLogger = bigLogger;
            argsStr = argsStr.substring(0, 2048);
        }
        // 调用的方法
        String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        Object result;
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String url = "", httpMethod = "", ip = "",operator = "";
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            url = request.getRequestURL().toString();
            httpMethod = request.getMethod();
            ip = WebUtil.getIpAddr(request);
        }
        long methodEnd;
        try {
            result = joinPoint.proceed();
            methodEnd = System.currentTimeMillis();
            //操作人 目前只加在了womp
            operator = MDC.get("operator");
            if(!classMethod.contains(NAME_UTIL)){
                defaultLogger.info(MSG, ip, url, methodEnd - startTime, httpMethod, classMethod,operator, argsStr, result);
            }
            // result的最大长度限制为500字符
            String resultLog = Objects.nonNull(result) ? result.toString() : "null";
            if (!Objects.isNull(resultLog) && resultLog.length() > 500){
                resultLog = resultLog.substring(0, 500);
            }
            long cosuming  = methodEnd - startTime;
            if (cosuming > requestAlertTime){
                try{
                    // 自定义mongo存储字段
                    // 时间默认为东八区当前时间，因为log4j2存储到mongo给的Date不是当前时区的，而是协调世界时UTC
                    MDC.put("createTime", new Date().toString());
                    MDC.put("url", url);
                    MDC.put("consuming", String.valueOf(cosuming));
                    MDC.put("classMethod", classMethod);
                    MDC.put("args", argsStr);
                    MDC.put("result", resultLog);
                    mongodbLogger.info("");
                }catch (Exception e){
                    log.error("log4j2存储到mongodb异常信息", e);
                }
            }
        } catch (Throwable e) {
            methodEnd = System.currentTimeMillis();
            defaultLogger.error(MSG, ip, url, methodEnd - startTime, httpMethod, classMethod, operator, argsStr, e);
            log.error("请求异常信息", e);
            throw e;
        }
        return result;
    }

    /**
     * 判断是否是json串
     *
     * @param content
     * @return
     */
    public boolean isJson(String content) {
        try {
            JSONObject.parseObject(content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
