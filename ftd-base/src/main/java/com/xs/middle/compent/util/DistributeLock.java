package com.xs.middle.compent.util;

/**
 * @author xiaos
 * @date 2019/11/18 10:39
 */
public interface DistributeLock {

    int tryLock(String key, String value);

    void unLock(String key, String value);
}
