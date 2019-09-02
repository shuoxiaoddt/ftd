package com.xs.middle.compent.ftd.mq.rabbitmq;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

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
    }
}
