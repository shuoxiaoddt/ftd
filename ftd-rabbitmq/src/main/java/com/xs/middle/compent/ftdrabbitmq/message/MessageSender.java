/*
 * Copyright (c) 2018 4PX Information Technology Co.,Ltd. All rights reserved.
 */
package com.xs.middle.compent.ftdrabbitmq.message;


import org.springframework.amqp.core.Message;

/**
 * 消息发送接口
 * @author wugx
 */
public interface MessageSender {

    /**
     * 发送消息，目前message只支持String类型（系统生成唯一id）
     * @param queueName
     * @param message
     */
    void send(String queueName, Object message);

    /**
     * 发送消息，目前message只支持String类型
     * @param queueName
     * @param message
     * @param messageId（消息id必须保证唯一）
     */
    void send(String queueName, Object message, String messageId);

    /**
     * 发送消息，目前message只支持String类型
     * @param queueName
     * @param message
     * @param messageId
     * @param expiration（有效期/ms）
     */
    void send(String queueName, Object message, String messageId, String expiration);

    /**
     * 发送消息
     * @param queueName
     * @param message
     */
    void send(String queueName, Message message);

}
