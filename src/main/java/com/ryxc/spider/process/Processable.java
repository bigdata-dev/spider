package com.ryxc.spider.process;

import com.ryxc.spider.domain.Page;

/**
 * Created by tonye0115 on 2016/7/1.
 */
public interface Processable {
    void process(Page page);
}
