package com.xs.middle.compent.constant;

import lombok.Data;
import lombok.Getter;
import org.springframework.amqp.core.ExchangeTypes;

/**
 * @author xiaos
 * @date 2019/9/3 18:59
 */
public enum  RabbitMqExchangeEnum {
    DEFAULT(ExchangeTypes.DIRECT,"default-exchange-xs"),

    DEFAULT_DEAD(ExchangeTypes.DIRECT,"default-dead-exchange-xs");

    RabbitMqExchangeEnum(String type, String name) {
        this.type = type;
        this.name = name;
    }
    @Getter
    private String type;

    @Getter
    private String name;

}
