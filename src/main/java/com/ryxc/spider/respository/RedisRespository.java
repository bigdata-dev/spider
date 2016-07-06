package com.ryxc.spider.respository;

import com.ryxc.spider.utils.RedisUtils;

/**
 * Created by tonye0115 on 2016/7/6.
 */
public class RedisRespository implements Respository {

    RedisUtils rdisUtils = new RedisUtils();

    public void add(String seedUrl) {
        this.rdisUtils.add(rdisUtils.key,seedUrl);
    }

    public Object poll() {
        return this.rdisUtils.poll(rdisUtils.key);
    }
}
