package com.ryxc.spider;

import com.ryxc.spider.utils.ThreadUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tonye0115 on 2016/8/27.
 */
public class TestLog4j {
    Logger logger = LoggerFactory.getLogger(TestLog4j.class);
    @Test
    public void test1() {
        while (true) {
            logger.info("当前时间戳：{}", System.currentTimeMillis());
            ThreadUtils.sleep(500);
        }
    }



}
