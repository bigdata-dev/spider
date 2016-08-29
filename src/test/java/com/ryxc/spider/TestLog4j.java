package com.ryxc.spider;

import com.ryxc.spider.utils.ThreadUtils;
import com.sun.xml.internal.rngom.binary.Pattern;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;

/**
 * Created by tonye0115 on 2016/8/27.
 */
public class TestLog4j {
    Logger logger = LoggerFactory.getLogger(TestLog4j.class);
    @Test
    public void test1() {
        String url = "http://item.jd.com/127768.html";
        while (true) {
            long startTime = System.currentTimeMillis();
            ThreadUtils.sleep(500);
            long currentTime = System.currentTimeMillis();
            logger.info("页面下载成功.url:[{}].耗时[{}]毫秒.当前时间戳:[{}]", url, currentTime - startTime, currentTime);

        }
    }

    @Test
    public void test2(){
        java.util.regex.Pattern pattern
                = java.util.regex.Pattern.compile("页面下载成功\\.url:\\[http://[a-zA-Z0-9]+\\.(.*)/.*\\]\\.耗时\\[([0-9]+)\\]毫秒\\.当前时间戳:\\[([0-9]+)\\]");
        Matcher matcher = pattern.matcher("页面下载成功.url:[http://item.jd.com/127768.html].耗时[500]毫秒.当前时间戳:[1472434203179]");
        if(matcher.find()){
            String topdomain = matcher.group(1);
            String uesTime = matcher.group(2);
            String time = matcher.group(3);
            System.out.println(topdomain+"--"+uesTime+"--"+time);
        }
    }



}
