package com.xs.middle.compent.demo;

/**
 * @author xiaos
 * @date 2019/11/28 13:59
 */
public class Client {

    public static void main(String[] args) {
        Salad salad = new Salad(new Apple("红富士"));
        //通过WeakReference的get()方法获取Apple
        Apple apple = salad.get();
        System.out.println("Apple:" + salad.get());
        System.gc();
        try {
            //休眠一下，在运行的时候加上虚拟机参数-XX:+PrintGCDetails，输出gc信息，确定gc发生了。
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(salad.get());
        //如果为空，代表被回收了
        if (salad.get() == null) {
            System.out.println("clear Apple。");
        }
    }
}
