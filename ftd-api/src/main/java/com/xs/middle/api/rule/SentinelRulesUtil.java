package com.xs.middle.api.rule;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaos
 * @date 2019/11/12 10:08
 */
public class SentinelRulesUtil {

    /**通过代码定义流量控制规则
     * @param resourceName
     */
    public static void initFlowQpsRule(String resourceName) {
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule(resourceName);
        // set limit qps to 20
        rule.setCount(20);
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setLimitApp("default");
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }


    public static void initDegradeRule(String resourceName) {
        List<DegradeRule> rules = new ArrayList<>();
        DegradeRule rule = new DegradeRule();
        rule.setResource(resourceName);
        // set threshold RT, 10 ms
        rule.setCount(10);
        rule.setGrade(RuleConstant.DEGRADE_GRADE_RT);
        rule.setTimeWindow(10);
        rules.add(rule);
        DegradeRuleManager.loadRules(rules);
    }

    public static void initSystemRule() {
        List<SystemRule> rules = new ArrayList<>();
        SystemRule rule = new SystemRule();
        rule.setHighestSystemLoad(10);
        rules.add(rule);
        SystemRuleManager.loadRules(rules);
    }



}
