package com.xs.middle.compent.util;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.amqp.core.MessageProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class RabbitMqUtil {

    /**
     * 获取消息被消费次数
     * @param messageProperties
     * @return
     */
    public static Integer getCount(MessageProperties messageProperties){

        Object obj = messageProperties.getHeaders().get("x-death");
        if (obj!=null){
            List list = (ArrayList) obj;
            if (list.size() != 0) {
                Object mapObj = list.get(0);
                return NumberUtils.toInt(((Map) mapObj).get("count").toString(),0);
            }
        }
        return 0;
    }
}
