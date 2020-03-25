package com.xs.middle.compent.demo;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * @author xiaos
 * @date 2019/11/28 14:02
 */
public class Entry extends WeakReference<ThreadLocal<?>> {
    Object value;

    Entry(ThreadLocal<?> k, Object v) {
        super(k);
        value = v;
    }
    Entry(ThreadLocal<?> k, Object v, ReferenceQueue referenceQueue) {
        super(k,referenceQueue);
        value = v;
    }
}