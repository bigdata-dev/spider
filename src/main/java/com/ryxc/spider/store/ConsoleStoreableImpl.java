package com.ryxc.spider.store;

import com.ryxc.spider.domain.Page;

/**
 * Created by tonye0115 on 2016/7/3.
 */
public class ConsoleStoreableImpl implements Storeable {

    public void store(Page page) {
        System.out.println("store:" + page.getUrl() + "--" +
                page.getFieldsMap().get("price") + "--" + page.getFieldsMap().get("title"));
    }
}
