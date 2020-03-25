package com.xs.middle.compent.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * @author xiaos
 * @date 2019/11/18 10:42
 */
public class ZookeeperClientUtil {

    final CuratorFramework curatorFramework;


    public ZookeeperClientUtil(String connectString, int sessionTimeoutMs, int connectionTimeoutMs, RetryPolicy retryPolicy) {
        this.curatorFramework = CuratorFrameworkFactory.newClient(connectString,sessionTimeoutMs,connectionTimeoutMs,retryPolicy);
    }

    public String createNode(String path,String value) throws Exception {
        curatorFramework.start();
        if(StringUtils.isNotBlank(value)){
            return curatorFramework.create().forPath(path, value.getBytes());
        }else {
            return curatorFramework.create().forPath(path);
        }
    }

    public void delNode(String path) throws Exception {
        curatorFramework.delete().forPath(path);
    }

    public void addWatch(){

    }
}
