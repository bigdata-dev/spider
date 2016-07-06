package com.ryxc.spider.respository;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by tonye0115 on 2016/7/6.
 */
public class QueueRespository implements Respository {

    Queue queue = new ConcurrentLinkedQueue<String>();

    public void add(String seedUrl) {
        this.queue.add(seedUrl);
    }

    public Object poll() {
        return this.queue.poll();
    }
}
