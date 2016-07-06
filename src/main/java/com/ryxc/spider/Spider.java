package com.ryxc.spider;

import com.ryxc.spider.domain.Page;
import com.ryxc.spider.download.Downloadable;
import com.ryxc.spider.download.HttpClientDownloadImpl;
import com.ryxc.spider.process.JdProcessImpl;
import com.ryxc.spider.process.Processable;
import com.ryxc.spider.respository.RedisRespository;
import com.ryxc.spider.respository.Respository;
import com.ryxc.spider.store.ConsoleStoreableImpl;
import com.ryxc.spider.store.Storeable;
import com.ryxc.spider.utils.ThreadUtils;

import java.util.List;

/**
 * Created by tonye0115 on 2016/7/1.
 */
public class Spider {

    private Downloadable downloadable;

    private Processable processable;

    private Storeable storeable;

    private String seedUrl;

    private Respository respository;

    public void setStoreable(Storeable storeable) {
        this.storeable = storeable;
    }

    public void setProcessable(Processable processable) {
        this.processable = processable;
    }

    public void setDownloadable(Downloadable downloadable) {
        this.downloadable = downloadable;
    }

    public void setSeedUrl(String seedUrl) {
        this.respository.add(seedUrl);
    }

    public Respository getRespository() {
        return respository;
    }

    public void setRespository(Respository respository) {
        this.respository = respository;
    }

    /**
     * 启动爬虫
     */
    public void start() {
        while(true) {
            String url = (String) this.respository.poll();
            if (org.apache.commons.lang.StringUtils.isEmpty(url)) {
                System.out.println("没有url, 休息一下~~~");
                ThreadUtils.sleep(3000);
            } else {
                Page page = this.download(url);
                this.process(page);
                List<String> urls = page.getUrls();
                for (String nextUrl :
                        urls) {
                    this.respository.add(nextUrl);
                }

                if (urls.isEmpty()) { //url为空表示商品
                    this.store(page);
                }
                ThreadUtils.sleep(1000);
            }
        }

    }

    /**
     * 下载页面代码
     *
     * @param url
     */
    public Page download(String url) {
        return downloadable.download(url);
    }

    /**
     * 解析页面数据
     *
     * @param page
     */
    public void process(Page page) {
        this.processable.process(page);
    }

    /**
     * 存储页面数据
     * @param page
     */
    public void store(Page page){
        this.storeable.store(page);
    }



    public static void main(String[] args) {
        Spider spider = new Spider();
        spider.setDownloadable(new HttpClientDownloadImpl());
        spider.setProcessable(new JdProcessImpl());
        spider.setStoreable(new ConsoleStoreableImpl());
        spider.setRespository(new RedisRespository());
        String url = "http://list.jd.com/list.html?cat=9987,653,655&page=64&go=0&JL=6_0_0&ms=6#J_main";
        spider.setSeedUrl(url);
        spider.start();
    }

}
