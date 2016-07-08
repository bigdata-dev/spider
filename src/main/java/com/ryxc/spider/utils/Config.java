package com.ryxc.spider.utils;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by tonye0115 on 2016/7/8.
 */
public class Config {
    static Properties properties;
    static{
        properties = new Properties();
        try {
            properties.load(Config.class.getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int nThread = Integer.parseInt(properties.getProperty("nThread"));
}
