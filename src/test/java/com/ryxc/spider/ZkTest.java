package com.ryxc.spider;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

/**
 * Created by tonye0115 on 2016/7/4.
 */
public class ZkTest {
    public static void main(String[] args) throws Exception{
        ZooKeeper zk = new ZooKeeper("z166.nat123.net:2181", 3000, null);
        waitUntilConnected(zk);
        System.out.println("=========创建节点===========");
        if(zk.exists("/test", false) == null)
        {
            zk.create("/test", "znode1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
       System.out.println("=============查看节点是否安装成功===============");
        System.out.println(new String(zk.getData("/test", false, null)));

       /* System.out.println("=========修改节点的数据==========");
        zk.setData("/test", "zNode2".getBytes(), -1);
        System.out.println("========查看修改的节点是否成功=========");
        System.out.println(new String(zk.getData("/test", false, null)));

        System.out.println("=======删除节点==========");
        zk.delete("/test", -1);
        System.out.println("==========查看节点是否被删除============");
        System.out.println("节点状态：" + zk.exists("/test", false));*/
        zk.close();
    }


    public static void waitUntilConnected(ZooKeeper zooKeeper) {

        CountDownLatch connectedLatch = new CountDownLatch(1);

        Watcher watcher = new ConnectedWatcher(connectedLatch);

        zooKeeper.register(watcher);


        if (ZooKeeper.States.CONNECTING == zooKeeper.getState()) {

            try {

                connectedLatch.await();

            } catch (InterruptedException e) {

                throw new IllegalStateException(e);

            }

        }

    }



    static class ConnectedWatcher implements Watcher {



        private CountDownLatch connectedLatch;



        ConnectedWatcher(CountDownLatch connectedLatch) {

            this.connectedLatch = connectedLatch;

        }



        public void process(WatchedEvent event) {

            if (event.getState() == Event.KeeperState.SyncConnected) {

                connectedLatch.countDown();

            }

        }

    }
}
