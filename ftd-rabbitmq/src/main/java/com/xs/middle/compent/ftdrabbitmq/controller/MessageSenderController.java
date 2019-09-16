package com.xs.middle.compent.ftdrabbitmq.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 39466
 * @date 2019/9/4 22:44
 * 大哥,写点注释怎么样?
 */
@RestController
@Slf4j
public class MessageSenderController {

    @Resource
    RabbitTemplate rabbitTemplate;
    @GetMapping("send/{exchange}/{queue}/{body}/{ttl}")
    public String sendMessageWithddl(@PathVariable("exchange") String exchange , @PathVariable("queue") String queue, @PathVariable("body")  String body , @PathVariable("ttl")  String ttl){
        log.info("send-{}-{}-{}-{}",exchange,queue,body,ttl);
        MessageProperties properties = new MessageProperties();
        properties.setExpiration(ttl);
        properties.setContentType("text/plain");
        Message message = new Message(body.getBytes(),properties);
        rabbitTemplate.send(exchange,queue,message);
        return "SUCCESS";
    }
}
