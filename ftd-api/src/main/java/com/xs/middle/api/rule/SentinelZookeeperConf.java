package com.xs.middle.api.rule;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.zookeeper.ZookeeperDataSource;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author xiaos
 * @date 2019/11/14 13:50
 */
@Configuration
public class SentinelZookeeperConf {

    private static final String ZOOKEEPER_REMOTE_ADDRESS = "10.104.6.71:2181";

    private final String groupId = "Ftd-Sentinel";

    private  final String flowDataId = "Flow";

    @PostConstruct
    public void init(){
        ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new ZookeeperDataSource<>(ZOOKEEPER_REMOTE_ADDRESS, groupId, flowDataId,
                source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {}));
        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
    }
}
