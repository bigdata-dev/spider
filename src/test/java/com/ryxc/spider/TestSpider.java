package com.ryxc.spider;

import com.ryxc.spider.domain.Page;
import com.ryxc.spider.download.HttpClientDownloadImpl;
import com.ryxc.spider.process.JdProcessImpl;
import com.ryxc.spider.store.ConsoleStoreableImpl;
import org.junit.Test;

/**
 * Created by tonye0115 on 2016/7/1.
 */
public class TestSpider {
    @Test
    public void testSpider(){
        Spider spider = new Spider();
        spider.setDownloadable(new HttpClientDownloadImpl());
        spider.setProcessable(new JdProcessImpl());
        spider.setStoreable(new ConsoleStoreableImpl());
        Page page = spider.download("http://item.jd.com/1647807.html");
        //System.out.println(page.getContent());
        spider.process(page);
        spider.store(page);
        //System.out.println(page.getFieldsMap());
    }
}
