package com.ryxc.spider;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.junit.Test;

import java.net.InetAddress;

/**
 * Created by tonye0115 on 2016/7/11.
 */
public class TestCurator {
    @Test
    public void test1() {
        String connectString = "hh166.all123.net:2181";
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(5000, 3);
        int sessionTimeoutMS =  5000;//会话超时时间，默认40S。这个值必须在4S--40S之间
        int connectionTimeoutMs = 10000;
        CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, sessionTimeoutMS, connectionTimeoutMs, retryPolicy);

        try {
            InetAddress localHost = InetAddress.getLocalHost();
            String ip = localHost.getHostAddress();
            client.start();
            client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                    .forPath("/spider/"+ip);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}

