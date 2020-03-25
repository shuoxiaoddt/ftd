package com.xs.middle.compent;

import com.xs.middle.compent.util.ZookeeperClientUtil;
import org.apache.curator.RetryPolicy;
import org.apache.curator.retry.RetryNTimes;
import org.junit.Before;
import org.junit.Test;

/**
 * @author xiaos
 * @date 2019/11/18 10:42
 */
public class ZkTest {
    ZookeeperClientUtil zookeeperClientUtil;
    @Before
    public void init(){
        zookeeperClientUtil = new ZookeeperClientUtil("10.104.6.46:2181",5000,5000, new RetryNTimes(3,5000));
    }

    @Test
    public void apiTest() throws Exception {
        System.out.println(zookeeperClientUtil.createNode("/mconfig",null));
    }


}
