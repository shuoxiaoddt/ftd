/*
 * Copyright (c) 2018 4PX Information Technology Co.,Ltd. All rights reserved.
 */
package com.xs.middle.compent.ftdrabbitmq.message.impl;


import com.xs.middle.compent.ftdrabbitmq.message.MessageSender;
import com.xs.middle.compent.util.WorkId;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageBuilderSupport;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;


@Service
public class MessageSenderImpl implements MessageSender, RabbitTemplate.ConfirmCallback {

    private Logger logger = LoggerFactory.getLogger("rabbitLog");


    @Resource
    private RabbitTemplate rabbitTemplate;

    @Value("${server.port}")
    private String port;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private CachingConnectionFactory cachingConnectionFactory;

    @Value("${spring.application.name:}")
    private String applicationName;

    @Value("${spring.rabbitmq.publisher-confirms:true}")
    private boolean publisherConfirms;

    private String key;

    /**
     * 存放未ack消息的key
     */
    private String needAckKey;

    @PostConstruct
    public void init() {
        cachingConnectionFactory.setPublisherConfirms(publisherConfirms);
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setConnectionFactory(cachingConnectionFactory);
        key = "ful:rabbitmq:" + getApplicationName();
        needAckKey = key + ":needack";
    }

    @Override
    public void send(String queue, Object message) {
        send(queue, message, null);
    }

    @Override
    public void send(String queueName, Object message, String messageId) {
        send(queueName, message, messageId, null);
    }

    @Override
    public void send(String queueName, Object body, String messageId, String expiration) {
        if (Objects.isNull(body)) {
            throw new RuntimeException("消息不能为空！");
        }

        MessageBuilderSupport<Message> messageMessageBuilderSupport = MessageBuilder
                .withBody(body.toString().getBytes())
                .setMessageId(messageId)
                .setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN)
                .setTimestamp(new Date());
        if (StringUtils.isNotBlank(expiration)) {
            messageMessageBuilderSupport.setExpiration(expiration);
        }
        Message message = messageMessageBuilderSupport.build();
        send(queueName, message);
    }

    @Override
    public void send(String queueName, Message message) {
        String messageId = message.getMessageProperties().getMessageId();
        if (StringUtils.isBlank(messageId)) {
            WorkId workId = new WorkId(10);
            messageId = workId.nextId() + port;
            message.getMessageProperties().setMessageId(messageId);
        }
        String body = new String(message.getBody());
        logger.info("messageId:{}  -> queueName:{}  -> body:{}", messageId, queueName, body);
        boolean isSave = true;
        boolean isSend = true;
        if (publisherConfirms) {

            try {
                CorrelationData correlationData = new CorrelationData(messageId);
                try {
                    String messageKey = key + ":" + messageId;
                    redisTemplate.opsForHash().putIfAbsent(messageKey, queueName, body);
                    redisTemplate.expire(messageKey, 10, TimeUnit.DAYS);
                    redisTemplate.opsForZSet().add(needAckKey, messageKey, System.currentTimeMillis());
                } catch (Exception e) {
                    isSave = false;
                    logger.error("保存发送消息失败：" + messageId, e);
                }
                rabbitTemplate.convertAndSend(queueName, message, correlationData);
            } catch (Exception e) {
                isSend = false;
                logger.error("发送消息失败：" + messageId, e);
            }
        } else {
            try {
                rabbitTemplate.convertAndSend(queueName, message);
            }catch (Exception e){
                isSend = false;
                logger.error("发送消息失败：" + messageId, e);
            }
        }
        if((!isSave && !isSend) || (!isSend && !publisherConfirms)){
            throw new RuntimeException("发送消息失败");
        }
        logger.info("消息{}发送成功", messageId);

    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            String messageKey = key + ":" + correlationData.getId();
            redisTemplate.opsForZSet().remove(needAckKey, messageKey);
            redisTemplate.delete(messageKey);
            logger.info("mq消息{}已确认", correlationData.getId());
        } else {
            logger.error("{}发送消息失败:{}", correlationData.getId(), cause);
        }
    }

    /**
     * 十分钟重试一次
     */
    @Scheduled(fixedDelay = 600000L)
    public void retry() {
        logger.info("重试发送mq消息");
        String lockKey = this.key + "lock";
        try {
            redisTemplate.opsForValue().set(lockKey, "1", 5, TimeUnit.MINUTES);
            Long count = redisTemplate.opsForZSet().zCard(needAckKey);
            Long tenMinusAgo = LocalDateTime.now().minusMinutes(10).toInstant(ZoneOffset.of("+8")).toEpochMilli() / 1000;
            for (int i = 0; i < count; ) {
                int max = i + 100;
                Set<String> messageKeys = redisTemplate.opsForZSet().rangeByScore(needAckKey, 0, tenMinusAgo, i, max);
                for (String messageKey : messageKeys) {
                    Map<Object, Object> entries = redisTemplate.opsForHash().entries(messageKey);
                    if (!CollectionUtils.isEmpty(entries)) {
                        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
                            int lastIndexOf = messageKey.lastIndexOf(":");
                            String messageId = messageKey.substring(lastIndexOf + 1);
                            send(entry.getKey().toString(), entry.getValue(), messageId);
                        }
                    }
                }
                i = max;
            }
        } finally {
            redisTemplate.delete(lockKey);
            logger.info("重发送失败mq消息完成");
        }
    }


    /**
     * 获取应用简称
     */
    private String getApplicationName() {
        if (StringUtils.isNotBlank(applicationName)) {
            String[] split = applicationName.split("-");
            if (split.length > 3) {
                return split[2];
            }
        }
        return StringUtils.trimToEmpty(applicationName);
    }
}
