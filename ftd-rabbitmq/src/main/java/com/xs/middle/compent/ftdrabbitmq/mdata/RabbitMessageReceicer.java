package com.xs.middle.compent.ftdrabbitmq.mdata;

import com.rabbitmq.client.Channel;
import com.xs.middle.compent.constant.RabbitMqExchangeEnum;
import com.xs.middle.compent.util.RabbitMqUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;


/**
 * @author 39466
 * @date 2019/9/4 21:58
 * RabbitMq 消费
 */
@Component
@Slf4j
public class RabbitMessageReceicer {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = {"big-handsome-queue","middle-handsome-queue","small-handsome-queue"})
    public void handSomeConsumer(String payLoad, Channel channel, Message message) throws IOException {
        try{
            Integer messageCount = RabbitMqUtil.getCount(message.getMessageProperties());
            message.getMessageProperties().setExpiration(String.valueOf(messageCount * 10000L));
        }catch (Exception e){
            log.error("error : {}", ExceptionUtils.getStackTrace(e));
        }finally {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
            rabbitTemplate.send(RabbitMqExchangeEnum.DEFAULT_DEAD.getName(),
                    message.getMessageProperties().getConsumerQueue()+"-dead",
                    message);
        }
    }
}
