package com.ryxc.spider.utils;

/**
 * Created by tonye0115 on 2016/7/6.
 */
public class ThreadUtils {
    public static void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
