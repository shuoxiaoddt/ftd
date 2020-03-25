package com.xs.middle.compent.demo;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * @author xiaos
 * @date 2019/11/28 11:17
 */
public class WeekReferenceDemo {

    public static void main(String[] args) throws InterruptedException {
        ReferenceQueue<ThreadLocal<?>> referenceQueue = new ReferenceQueue();
        Entry entry = new Entry(ThreadLocal.withInitial(() -> {
            return "hello";
        }),"world",referenceQueue);
        System.out.println("start:"+entry.get().get());
        System.gc();
        Thread.sleep(1000);
        System.out.println("gc-end:"+entry.get());
        System.out.println("ref:"+referenceQueue.poll());
    }

}
