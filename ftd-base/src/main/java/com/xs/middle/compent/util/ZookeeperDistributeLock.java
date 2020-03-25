package com.xs.middle.compent.util;

/**
 * @author xiaos
 * @date 2019/11/18 10:41
 */
public class ZookeeperDistributeLock implements DistributeLock {


    @Override
    public int tryLock(String key, String value) {
        return 0;
    }

    @Override
    public void unLock(String key, String value) {

    }
}
