package com.xs.middle.compent.ftd.mq.rabbitmq;

import com.google.common.collect.Maps;
import com.xs.middle.compent.ftd.constant.RabbitMqExchangeEnum;
import com.xs.middle.compent.ftd.constant.RabbitMqQueueEnum;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xiaos
 * @date 2019/9/2 17:54
 */
@Configuration
public class RabbitMqConfiguration {

    @Resource
    private ConnectionFactory connectionFactory;

    private RabbitAdmin rabbitAdmin;

    private static final String DEAD_SUFFIX = "-dead";

    @PostConstruct
    protected void init(){
        rabbitAdmin = new RabbitAdmin(connectionFactory);
        List<QueueWarpper> queueWarppers = initQueueMap();
        declareExchange();
        declareQueues(queueWarppers);
        declareBinding(queueWarppers);
    }

    private void declareBinding(List<QueueWarpper> queueWarppers) {
        queueWarppers.forEach(item -> {
            Binding queueBinding = BindingBuilder
                    .bind(item.getQueue())
                    .to(ExchangeBuilder.directExchange(RabbitMqExchangeEnum.DEFAULT.getName()).build())
                    .with(item.getQueue().getName()).noargs();
            Binding deadQueueBinding = BindingBuilder
                    .bind(item.getDeadQueue())
                    .to(ExchangeBuilder.directExchange(RabbitMqExchangeEnum.DEFAULT_DEAD.getName()).build())
                    .with(item.getDeadQueue().getName()).noargs();
            rabbitAdmin.declareBinding(queueBinding);
            rabbitAdmin.declareBinding(deadQueueBinding);
        });
    }

    private void declareQueues(List<QueueWarpper> queueWarppers) {
        queueWarppers.forEach(item -> {
            rabbitAdmin.declareQueue(item.getQueue());
            rabbitAdmin.declareQueue(item.getDeadQueue());
        });
    }

    private void declareExchange() {
        RabbitMqExchangeEnum[] exchangeEnums = RabbitMqExchangeEnum.values();
        List<Exchange> exchanges = new ArrayList<>();
        for(RabbitMqExchangeEnum exchangeEnum : exchangeEnums){
            if(ExchangeTypes.DIRECT.equals(exchangeEnum.getType())){
                exchanges.add(ExchangeBuilder.directExchange(exchangeEnum.getName()).build());
            }
        }
        exchanges.forEach(exchange -> {
            rabbitAdmin.declareExchange(exchange);
        });
    }

    private List<QueueWarpper> initQueueMap() {
        List<QueueWarpper> queueList = new ArrayList<>();

        RabbitMqQueueEnum[] queueEnums = RabbitMqQueueEnum.values();
        for(RabbitMqQueueEnum queueEnum : queueEnums){
            QueueWarpper warpper = new QueueWarpper();
            Map<String,Object> quequArgs = buildQueueArgs(queueEnum);
            Queue queue = new Queue(queueEnum.getName(),true,false,false,quequArgs);

            Queue deadQueue = null;
            if(queueEnum.isNeedDeadLetter()){
                Map<String,Object> deadQueueArgs = buildDeadQueueArgs(queueEnum);
                deadQueue = new Queue(queueEnum.getName() + DEAD_SUFFIX,true,false,false,deadQueueArgs);
            }
            warpper.setQueue(queue);
            warpper.setDeadQueue(deadQueue);
            warpper.setExchangeEnum(queueEnum.getExchange());
            queueList.add(warpper);
        }
        return queueList;
    }

    private Map<String, Object> buildQueueArgs(RabbitMqQueueEnum queueEnum) {
        Map<String,Object> args = Maps.newHashMap();
        if(queueEnum.isNeedDeadLetter()){
            args.put("x-dead-letter-exchange",RabbitMqExchangeEnum.DEFAULT_DEAD.getName());
            args.put("x-dead-letter-routing-key",queueEnum.getName()+DEAD_SUFFIX);
        }
        return args;
    }

    private Map<String, Object> buildDeadQueueArgs(RabbitMqQueueEnum queueEnum) {
        Map<String,Object> args = Maps.newHashMap();
        if(queueEnum.isNeedDeadLetter()){
            args.put("x-dead-letter-exchange",RabbitMqExchangeEnum.DEFAULT.getName());
            args.put("x-dead-letter-routing-key",queueEnum.getName());
            args.put("x-message-ttl",queueEnum.getDeadLetterTtl());
        }
        return args;
    }
}
