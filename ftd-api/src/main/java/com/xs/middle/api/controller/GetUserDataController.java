package com.xs.middle.api.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.xs.middle.api.rule.SentinelRulesUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaos
 * @date 2019/11/11 14:43
 */
@RestController
@RequestMapping("/ftd/get/userinfo")
public class GetUserDataController {

    @RequestMapping("/name")
    public String getName() throws InterruptedException {
        Entry entry = null;
        try {
            // 资源名可使用任意有业务语义的字符串
            SentinelRulesUtil.initFlowQpsRule("hello-world");
            entry = SphU.entry("hello-world");
            TimeUnit.SECONDS.sleep(1);
            return "hello,rest";
        } catch (BlockException e1) {
            System.out.println("服务熔断");
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }
        return "null";

    }

    @RequestMapping("/nameResource")
    @SentinelResource(value = "TestResource",blockHandler="nameResourceBlock")
    public String nameResource() throws InterruptedException {
        // 资源名可使用任意有业务语义的字符串
        TimeUnit.SECONDS.sleep(1);
        return "hello,rest";
    }
    public String nameResourceBlock(){
        return "==Block==";
    }

}
