package com.xs.middle.compent.ftd;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author xiaos
 * @date 2019/9/6 11:07
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FtdApplication.class)
public class RabbitMqSenderTest {

    @Rule
    ContiPerfRule contiPerfRule = new ContiPerfRule();

    @Resource
    RabbitTemplate rabbitTemplate;

    @Test
    @PerfTest(invocations = 100 , threads = 10)
    public void testSender(){

    }
}
