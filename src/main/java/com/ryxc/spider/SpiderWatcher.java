package com.ryxc.spider;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by tonye0115 on 2016/7/11.
 */
public class SpiderWatcher implements Watcher {
    Logger logger = LoggerFactory.getLogger(SpiderWatcher.class);
    List<String> childrenList;
    CuratorFramework client;

    public SpiderWatcher() {
        String connectString = "ryxc166:2181";
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(5000, 3);
        int sessionTimeoutMS =  5000;//会话超时时间，默认40S。这个值必须在4S--40S之间
        int connectionTimeoutMs = 10000;
        client = CuratorFrameworkFactory.newClient(connectString, sessionTimeoutMS, connectionTimeoutMs, retryPolicy);

        client.start();
        //监视spider下面所有子节点的变化情况，注册监视器，注意，这个监视器单次有效，需要重复注册
        try {
            childrenList = client.getChildren().usingWatcher(this).forPath("/spider");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void process(WatchedEvent watchedEvent) {
        try {
            List<String> newChildrenList = client.getChildren().usingWatcher(this).forPath("/spider");
            for (String node : childrenList
                    ) {
                if (!newChildrenList.contains(node)) {
                    logger.info("节点消失:"+node);
                }
            }

            for (String node : newChildrenList
                    ) {
                if(!childrenList.contains(node)){
                    logger.info("新增节点:"+node);
                }
            }
            this.childrenList = newChildrenList;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        SpiderWatcher spiderWatcher = new SpiderWatcher();
        spiderWatcher.run();
    }

    private void run() {
        while (true){
            ;
        }
    }
}
