package com.xs.middle.compent.ftd.mq.rabbitmq;

import com.xs.middle.compent.ftd.constant.RabbitMqExchangeEnum;
import lombok.Data;
import org.springframework.amqp.core.Queue;

/**
 * @author xiaos
 * @date 2019/9/3 18:47
 */
@Data
public class QueueWarpper {

    private Queue queue;

    private Queue deadQueue;

    private RabbitMqExchangeEnum exchangeEnum;
}
