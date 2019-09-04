package com.xs.middle.compent.ftd.constant;


import lombok.Getter;

/**
 * @author xiaos
 * @date 2019/9/3 13:48
 */
public enum  RabbitMqQueueEnum {

    BIG_HANDSOME_QUEUE("big-handsome-queue",RabbitMqExchangeEnum.DEFAULT,0L,true,10000L),

    MIDDLE_HANDSOME_QUEUE("middle-handsome-queue",RabbitMqExchangeEnum.DEFAULT,0L,true,30000L),

    SMALL_HANDSOME_QUEUE("small-handsome-queue",RabbitMqExchangeEnum.DEFAULT,0L,true,60000L);

    private final String name;

    private RabbitMqExchangeEnum exchange;

    private long ttl;

    private boolean needDeadLetter;

    private long deadLetterTtl;

    RabbitMqQueueEnum(String name,RabbitMqExchangeEnum exchange, long ttl, boolean needDeadLetter, long deadLetterTtl) {
        this.name = name;
        this.exchange = exchange;
        this.ttl = ttl;
        this.needDeadLetter = needDeadLetter;
        this.deadLetterTtl = deadLetterTtl;
    }

    public String getName() {
        return name;
    }

    public RabbitMqExchangeEnum getExchange() {
        return exchange;
    }

    public long getTtl() {
        return ttl;
    }

    public boolean isNeedDeadLetter() {
        return needDeadLetter;
    }

    public long getDeadLetterTtl() {
        return deadLetterTtl;
    }
}
