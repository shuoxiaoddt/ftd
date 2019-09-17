package com.xs.middle.compent.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author wugx
 * @date 2018/3/18 11:29
 * 根据 全球数据中心 和机器ID ,不同的洲的时间不同生成有序的19位唯一ID 获得下一个ID (该方法是线程安全的)
 *
 *
 * <pre>
 * 0  -  0000000000  0000000000  0000000000  0000000000  0  -  00000  -  00000  -  000000000000
 * 1位标识|-----------这41位是时间戳-------------|---数据中心|-机器Id-|12位序列(自增的)，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒
 *
 *     第1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0
 *     后面的41位时间截(毫秒级)，注意，41位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截  -  开始时间截)得到的值，这里的的开始时间截，一般是我们的id生成器开始使用的时间，由我们程序来指定的（如下下面程序IdWorker类的startTime属性）。41位的时间截，可以使用69年，年T  =  (1L  <<  41)  /  (1000L  *  60  *  60  *  24  *  365)  =  69
 *     后面的11位的数据（5位）+机器位(6位)，可以部署在1024个节点，包括5位datacenterId和6位workerId
 *     12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生4096个ID序号
 *     生成的ID加起来刚好64位，为一个Long型。
 *     整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)，并且效率较高，经测试，GIdUtil每秒能够产生26万ID左
 *
 * 10位的数据机器位，可以部署在1024个节点
 * 12位序列，同一时间截，同一机器，可以生成4096个id
 *   -1L  ^  (-1L  <<  n)表示占n个bit的数字的最大值是多少。例如：-1L  ^  (-1L  <<  2)等于10进制的3  ，即二进制的11表示十进制3。
 * 注意：计算机存放数字都是存放数字的补码，正数的原码、补码、反码都一样，负数的补码是其反码加一。符号位做取反操作时不变，做逻辑与、或、非、异或操作时要参与运算。
 * 再来个例子：
 * -1L原码  :  1000  0001
 * -1L反码  :  1111  1110
 * -1L补码  :  1111  1111
 * -1L<<5  :  1110  0000
 * 1111  1111  ^  1110  0000  :  0001  1111
 * 0001  1111是正数，所以补码、反码、原码都一样，所以0001  1111是31
 * ((timestamp  -  twepoch)  <<  timestampLeftShift)  |  (datacenterId  <<  datacenterIdShift)  |  (workerId  <<  workerIdShift)  |  sequence
 * 0000  0000  0001  左移动0位    0000  0000  0001
 *
 * 0000  0000  0010  左移动4位    0000  0010  0000
 *
 * 0000  0000  0101  左移动8位    0101  0000  0000
 *
 * 通过移位后得到的
 *
 *   0000  0000  0001
 *
 *   0000  0010  0000
 *
 *   0101  0000  0000
 * 在做或运算后得到0101  0010  0001。
 * </pre>
 * @return SnowflakeId
 */
public class WorkId {

    // ==============================Fields===========================================

    /**
     * 机器id所占的位数
     */
    private final long workerIdBits = 8L;

    /**
     * 数据中心标识id所占的位数
     */
    private final long datacenterIdBits = 4L;

    /**
     * 支持的最大机器id，结果是512 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
     */
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

    /**
     * 支持的最大数据中心标识id，结果是64
     */
    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

    /**
     * 序列在id中占的位数
     */
    private final long sequenceBits = 12L;

    /**
     * 机器ID向左移12位
     */
    private final long workerIdShift = sequenceBits;

    /**
     * 数据标识id向左移17位(12+5)
     */
    private final long datacenterIdShift = sequenceBits + workerIdBits;

    /**
     * 时间截向左移22位(5+5+12)
     */
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    /**
     * 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
     */
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    /**
     * 工作机器ID(0~511)
     */
    private volatile long workerId;

    /**
     * 数据中心ID(0~15)
     */
    private volatile long datacenterId;

    /**
     * 毫秒内序列(0~4095)
     */
    private static volatile long sequence = 0L;

    /**
     * 上次生成ID的时间截
     */
    private static volatile long lastTimestamp = -1L;

    // ==============================Constructors=====================================

    /**
     * 构造函数
     *
     * @param datacenterId 数据中心ID (0~15)
     */
    public WorkId(long datacenterId) {

        long workerId;

        // 获取本地ip的末端地址
        try {

            String ip[] = IpUtil.getLocalIP().split("\\.");

            workerId = Long.valueOf(ip[ip.length - 1]);

        } catch (Exception e) {

            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));

        }

        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));


        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));

        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    // ==============================Methods==========================================

    /**
     * 获得下一个ID (该方法是线程安全的)
     *
     * @return SnowflakeId
     */
    public long nextId() {
    	synchronized (WorkId.class) {
    		long timestamp = timeGen();

            // 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
            if (timestamp < lastTimestamp) {
                throw new RuntimeException(String.format(
                        "Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
            }

            // 如果是同一时间生成的，则进行毫秒内序列
            if (lastTimestamp == timestamp) {
                sequence = (sequence + 1) & sequenceMask;
                // 毫秒内序列溢出
                if (sequence == 0) {
                    // 阻塞到下一个毫秒,获得新的时间戳
                    timestamp = tilNextMillis(lastTimestamp);
                }
            }
            // 时间戳改变，毫秒内序列重置
            else {
                sequence = 0L;
            }

            // 上次生成ID的时间截
            lastTimestamp = timestamp;

            // 移位并通过或运算拼到一起组成64位的ID

            /* 开始时间截 (2018-01-01) */
            AtomicLong twepoch = new AtomicLong(1514736000000L);
            return Long.parseLong(String.valueOf(((timestamp - twepoch.get()) << timestampLeftShift)
                    | (datacenterId << datacenterIdShift) | (workerId << workerIdShift) | sequence).replace("-", ""));
		}
        
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     *
     * @return 当前时间(毫秒)
     */
    private long timeGen() {
        return System.currentTimeMillis();
    }

}
