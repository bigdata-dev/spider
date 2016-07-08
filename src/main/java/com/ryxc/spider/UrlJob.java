package com.ryxc.spider;

import com.ryxc.spider.utils.RedisUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by tonye0115 on 2016/7/8.
 */
public class UrlJob implements Job {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    RedisUtils redisUtils = new RedisUtils();


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("UrlJob 调用了!!!");
        List<String> startUrls = redisUtils.lrange(RedisUtils.start_url, 0, -1);
        for (String startUrl : startUrls
                ) {
            redisUtils.add(RedisUtils.key,startUrl);
        }
    }
}
