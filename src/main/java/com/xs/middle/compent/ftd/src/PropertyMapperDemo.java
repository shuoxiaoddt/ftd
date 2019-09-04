package com.xs.middle.compent.ftd.src;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.context.properties.PropertyMapper;


/**
 * @author xiaos
 * @date 2019/9/4 16:51
 * String 一个 设值工具
 * @see RabbitAutoConfiguration
 */
public class PropertyMapperDemo {
    public static void main(String[] args) {
        PropertyMapperDemo demo = new PropertyMapperDemo();
        System.out.println(demo.convert().toString());
    }
    public B convert(){
        PropertyMapper map = PropertyMapper.get();
        PropertyMapperDemo.A a = new PropertyMapperDemo.A("nameA");
        PropertyMapperDemo.B b = new PropertyMapperDemo.B("nameB");
        map.from(a :: getNameA).whenNonNull().to(b :: setNameB);
        return b;
    }
    @Data
    @ToString
    @AllArgsConstructor
    private class A{
        private String nameA;

    }
    @Data
    @ToString
    @AllArgsConstructor
    private class B{

        private String nameB;
    }
}
