package com.ryxc.spider.respository;

/**
 * Created by tonye0115 on 2016/7/6.
 */
public interface Respository {

    void add(String seedUrl);

    Object poll();
}
