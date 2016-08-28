package com.ryxc.spider;

import com.ryxc.spider.domain.Page;
import com.ryxc.spider.download.Downloadable;
import com.ryxc.spider.download.HttpClientDownloadImpl;
import com.ryxc.spider.process.JdProcessImpl;
import com.ryxc.spider.process.Processable;
import com.ryxc.spider.respository.RedisRespository;
import com.ryxc.spider.respository.Respository;
import com.ryxc.spider.store.ConsoleStoreableImpl;
import com.ryxc.spider.store.HbaseStoreableImpl;
import com.ryxc.spider.store.Storeable;
import com.ryxc.spider.utils.Config;
import com.ryxc.spider.utils.ThreadUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by tonye0115 on 2016/7/1.
 */
public class Spider {

    Logger logger = LoggerFactory.getLogger(Spider.class);

    private Downloadable downloadable = new HttpClientDownloadImpl();

    private Processable processable;

    private Storeable storeable = new ConsoleStoreableImpl();

    private String seedUrl;

    private Respository respository = new RedisRespository();

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


    ExecutorService threadPool = Executors.newFixedThreadPool(Config.nThread);

    public Spider(){
        String connectString = "192.168.116.161:2181";
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(5000, 3);
        int sessionTimeoutMS =  5000;//会话超时时间，默认40S。这个值必须在4S--40S之间
        int connectionTimeoutMs = 10000;
        CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, sessionTimeoutMS, connectionTimeoutMs, retryPolicy);

        try {
            InetAddress localHost = InetAddress.getLocalHost();
            String ip = localHost.getHostAddress();
            client.start();
            client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                    .forPath("/spider/"+ip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动爬虫
     */
    public void start() {
        logger.info("开始执行爬虫");
        while(true) {
            final String url = (String) this.respository.poll();
            if (org.apache.commons.lang.StringUtils.isEmpty(url)) {
                logger.info("没有url, 休息一下~~~");
                ThreadUtils.sleep(3000);
            } else {
                threadPool.execute(new Runnable() {
                                       public void run() {
                                           Page page = Spider.this.download(url);
                                           Spider.this.process(page);
                                           List<String> urls = page.getUrls();
                                           for (String nextUrl :
                                                   urls) {
                                               Spider.this.respository.add(nextUrl);
                                           }

                                           if (urls.isEmpty()) { //url为空表示商品
                                               logger.info("######## page.getFieldsMap:"+page.getFieldsMap());
                                               if(StringUtils.isNotBlank(page.getFieldsMap().get("picurl"))) {
                                                   Spider.this.store(page);
                                               }
                                           }

                                           ThreadUtils.sleep(1000);
                                       }
                                   });



            }
        }

    }

    private void check() {
        logger.info("开始进行配置检查...");
        if(processable==null){
            String message = "没有设置默认解析类....";
            logger.error(message);
            throw new RuntimeException(message);
        }
        logger.info("=================================================");
        logger.info("downloadable的实现类是：{}",downloadable.getClass().getName());
        logger.info("processable的实现类是：{}",processable.getClass().getName());
        logger.info("storeable的实现类是：{}",storeable.getClass().getName());
        logger.info("repository的实现类是：{}",respository.getClass().getName());
        logger.info("=================================================");

        try {
            this.getRespository().poll();
        }catch (Exception e){
            String message = String.format("检查共享仓库%s失败,错误信息：%s",
                    new String[]{respository.getClass().getName(),e.getLocalizedMessage()});
            logger.error(message);
            throw new RuntimeException(message);
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
        spider.setStoreable(new HbaseStoreableImpl());
        spider.setProcessable(new JdProcessImpl());
        String url = "http://list.jd.com/list.html?cat=9987,653,655&page=64&go=0&JL=6_0_0&ms=6#J_main";
        spider.check();
        spider.setSeedUrl(url);
        spider.start();
    }

}
