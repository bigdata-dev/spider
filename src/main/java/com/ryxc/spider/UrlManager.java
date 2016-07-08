package com.ryxc.spider;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Created by tonye0115 on 2016/7/8.
 */
public class UrlManager {
    public static void main(String[] args) {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            String simpleName = UrlJob.class.getSimpleName();
            JobDetail jobDetail = new JobDetail(simpleName,Scheduler.DEFAULT_GROUP,UrlJob.class);
            CronTrigger trigger = new CronTrigger(simpleName, Scheduler.DEFAULT_GROUP, "0 06 21 ? * *  ");
            scheduler.scheduleJob(jobDetail,trigger);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
