package com.xs.middle.compent.ftd.mq.rabbitmq;

import com.xs.middle.compent.ftd.constant.RabbitMqExchangeEnum;
import com.xs.middle.compent.ftd.constant.RabbitMqQueueEnum;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaos
 * @date 2019/9/2 17:54
 */
@Configuration
public class RabbitMqConfiguration {

    @Resource
    private ConnectionFactory connectionFactory;

    private RabbitAdmin rabbitAdmin;

    @PostConstruct
    protected void init(){
        rabbitAdmin = new RabbitAdmin(connectionFactory);
        List<QueueWarpper> queueWarppers = initQueueMap();
        declareExchange();
        declareQueues(queueWarppers);
        declareBinding(queueWarppers);
    }

    private void declareBinding(List<QueueWarpper> queueWarppers) {

    }

    private void declareQueues(List<QueueWarpper> queueWarppers) {

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
            Queue queue = new Queue(queueEnum.getName());
            warpper.setTtl(queueEnum.getTtl());
            Queue deadQueue = null;
            if(queueEnum.isNeedDeadLetter()){
                deadQueue = new Queue(queueEnum.getName());
                warpper.setDeadLetterTtl(warpper.getDeadLetterTtl());
            }
            warpper.setQueue(queue);
            warpper.setDeadQueue(deadQueue);
            warpper.setExchangeEnum(queueEnum.getExchange());
            queueList.add(warpper);
        }
        return queueList;
    }
}
