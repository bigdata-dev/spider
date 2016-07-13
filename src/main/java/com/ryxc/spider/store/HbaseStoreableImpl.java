package com.ryxc.spider.store;

import com.ryxc.spider.domain.Page;
import com.ryxc.spider.utils.HbaseUtils;
import com.ryxc.spider.utils.RedisUtils;

import java.io.IOException;
import java.util.Map;

/**
 * Created by tonye0115 on 2016/7/3.
 */
public class HbaseStoreableImpl implements Storeable {
    RedisUtils redisUtils = new RedisUtils();
    HbaseUtils hbaseUtils = new HbaseUtils();
    /**
     * 在hbase中存储的时候，分为两个列族
     * 标题、价格、图片地址
     * 规格参数
     * create 'spider','goodsinfo','spec'
     * alter 'spider',{NAME=>'goodsinfo',VERSIONS=>30}
     * alter 'spider',{NAME=>'spec',VERSIONS=>30}
     * @param page
     */
    public void store(Page page) {
        String goodsId = page.getGoodsId();
        Map<String, String> values = page.getFieldsMap();
        try {
            hbaseUtils.put(HbaseUtils.TABLE_NAME, goodsId, HbaseUtils.COLUMNFAMILY_1, HbaseUtils.COLUMNFAMILY_1_DATA_URL, page.getUrl());
            hbaseUtils.put(HbaseUtils.TABLE_NAME, goodsId, HbaseUtils.COLUMNFAMILY_1, HbaseUtils.COLUMNFAMILY_1_PIC_URL, values.get("picurl"));
            hbaseUtils.put(HbaseUtils.TABLE_NAME, goodsId, HbaseUtils.COLUMNFAMILY_1, HbaseUtils.COLUMNFAMILY_1_PRICE, values.get("price"));
            hbaseUtils.put(HbaseUtils.TABLE_NAME, goodsId, HbaseUtils.COLUMNFAMILY_1, HbaseUtils.COLUMNFAMILY_1_TITLE, values.get("title"));
            hbaseUtils.put(HbaseUtils.TABLE_NAME, goodsId, HbaseUtils.COLUMNFAMILY_2, HbaseUtils.COLUMNFAMILY_2_PARAM, values.get("spec"));

            redisUtils.add("solr_index", goodsId);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
