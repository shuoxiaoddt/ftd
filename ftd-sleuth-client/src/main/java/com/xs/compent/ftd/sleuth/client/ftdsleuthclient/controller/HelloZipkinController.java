package com.xs.compent.ftd.sleuth.client.ftdsleuthclient.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import zipkin2.reporter.Sender;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author xiaos
 * @date 2020/1/9 14:52
 */
@RestController
@RequestMapping("/zipkin")
public class HelloZipkinController {


    @Resource
    Sender sender;
    @RequestMapping("/hello/{name}")
    public String hello(@PathVariable String name, HttpServletRequest request){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/provider/getRealName/"+name;
        String realName = restTemplate.getForObject(url, String.class);
        return realName;
    }
}
