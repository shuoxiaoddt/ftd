package com.xs.middle.compent.demo;

import java.lang.ref.WeakReference;

/**
 * @author xiaos
 * @date 2019/11/28 13:58
 */
public class Salad extends WeakReference<Apple> {
    public Salad(Apple apple) {
        super(apple);
    }
}