package com.ryxc.spider.download;

import com.ryxc.spider.domain.Page;

/**
 * Created by tonye0115 on 2016/7/1.
 */
public interface Downloadable {
    Page download(String url);
}
