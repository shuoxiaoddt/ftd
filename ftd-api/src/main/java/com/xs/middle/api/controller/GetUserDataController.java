package com.xs.middle.api.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author xiaos
 * @date 2019/11/11 14:43
 */
@RestController
@RequestMapping("/ftd/get/userinfo")
public class GetUserDataController {

    @RequestMapping("/name")
    @SentinelResource("hello")
    public String getName() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        return "hello,rest";
    }
}
