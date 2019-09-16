package com.xs.middle.compent.ftdrabbitmq.mdata;

import com.rabbitmq.client.Channel;
import com.xs.middle.compent.ftdmiddle.constant.RabbitMqExchangeEnum;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @author 39466
 * @date 2019/9/4 21:58
 * RabbitMq 消费
 */
@Component
public class RabbitMessageReceicer {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = {"big-handsome-queue","middle-handsome-queue","small-handsome-queue"})
    public void handSomeConsumer(String payLoad, Channel channel, Message message) throws IOException {
        LocalDateTime now = LocalDateTime.now();
        String expiration = obtainExpiration(message);
        System.out.println("handsome-queue:"+expiration+" body : "+ payLoad +" ttl : "+message.getMessageProperties().getExpiration()+" current : " + now.getMinute() + ":" +now.getSecond());
        message.getMessageProperties().setExpiration(String.valueOf(Integer.parseInt("5000") + Integer.parseInt(expiration)));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        rabbitTemplate.send(RabbitMqExchangeEnum.DEFAULT_DEAD.getName(),
                message.getMessageProperties().getConsumerQueue()+"-dead",
                message);
    }
    private String obtainExpiration( Message message){
        Object xDeath = message.getMessageProperties().getHeaders().get("x-death");
        List<HashMap<String,String>> lsx = (ArrayList)xDeath;
        HashMap<String,String> s = lsx.get(1);
        return s.get("original-expiration");
    }
}
