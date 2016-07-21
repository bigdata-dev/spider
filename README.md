spider项目主要包括三块内容
1. 运行爬取数据部分 
    下载模块使用 httpclient
     解析模块使用 htmlcleaner
     存储模块使用 hbase
2. 运行url定时调度部分
     （1） redis 实现url队列
     （2） quartz 实现每天爬取一次 
3.运行服务监控
      curator+zookeeper
