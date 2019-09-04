package com.xs.middle.compent.ftd.constant;


import lombok.Getter;

/**
 * @author xiaos
 * @date 2019/9/3 13:48
 */
public enum  RabbitMqQueueEnum {

    BIG_HANDSOME_QUEUE("big-handsome-queue",RabbitMqExchangeEnum.DEFAULT,0L,true,60000L);

    @Getter
    private String name;

    @Getter
    private RabbitMqExchangeEnum exchange;

    @Getter
    private long ttl;

    @Getter
    private boolean needDeadLetter;

    @Getter
    private long deadLetterTtl;

    RabbitMqQueueEnum(String name,RabbitMqExchangeEnum exchange, long ttl, boolean needDeadLetter, long deadLetterTtl) {
        this.name = name;
        this.exchange = exchange;
        this.ttl = ttl;
        this.needDeadLetter = needDeadLetter;
        this.deadLetterTtl = deadLetterTtl;
    }
}
