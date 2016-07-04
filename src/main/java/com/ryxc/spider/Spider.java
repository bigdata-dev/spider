package com.ryxc.spider;

import com.ryxc.spider.domain.Page;
import com.ryxc.spider.download.Downloadable;
import com.ryxc.spider.process.Processable;
import com.ryxc.spider.store.Storeable;

/**
 * Created by tonye0115 on 2016/7/1.
 */
public class Spider {

    private Downloadable downloadable;

    private Processable processable;

    private Storeable storeable;

    public void setStoreable(Storeable storeable) {
        this.storeable = storeable;
    }

    public void setProcessable(Processable processable) {
        this.processable = processable;
    }

    public void setDownloadable(Downloadable downloadable) {
        this.downloadable = downloadable;
    }

    /**
     * 启动爬虫
     */
    public void start() {
        //download();
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
}
