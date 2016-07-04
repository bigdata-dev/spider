package com.ryxc.spider.store;

import com.ryxc.spider.domain.Page;

/**
 * Created by tonye0115 on 2016/7/3.
 */
public class HbaseStoreableImpl implements Storeable {
    /**
     * 在hbase中存储的时候，分为两个列族
     * 标题、价格、图片地址
     * 规格参数
     * create 'spider','goodsinfo','spc'
     * alter 'spider',{NAME=>'goodsinfo',VERSIONS=>30}
     * @param page
     */
    public void store(Page page) {

    }
}
