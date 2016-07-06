package com.ryxc.spider.utils;

import com.ryxc.spider.domain.Page;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;

/**
 * Created by tonye0115 on 2016/7/1.
 */
public class PageUtils {
    static Logger logger = LoggerFactory.getLogger(PageUtils.class);
    public static String getContent(String url){
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        CloseableHttpClient client = httpClientBuilder.build();

        Page page = new Page();
        HttpGet httpGet = new HttpGet(url);
        String content = null;
        try {
            long startTime = System.currentTimeMillis();
            CloseableHttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            content = EntityUtils.toString(entity);
            //System.out.println(EntityUtils.toString(entity));
            page.setContent(content);
            logger.info("页面下载成功，消耗时间：{},url：{}",System.currentTimeMillis()-startTime,url);
        } catch (Exception e) {
            logger.error("页面下载失败，url：{},具体的错误内容：{}",url,e.getMessage());
        }
        return content;
    }
}
