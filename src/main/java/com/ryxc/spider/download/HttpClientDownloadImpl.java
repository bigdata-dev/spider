package com.ryxc.spider.download;

import com.ryxc.spider.domain.Page;
import com.ryxc.spider.utils.PageUtils;

/**
 * Created by tonye0115 on 2016/7/1.
 */
public class HttpClientDownloadImpl implements Downloadable {

    public Page download(String url) {
        String content = PageUtils.getContent(url);
        Page page = new Page();
        page.setUrl(url);
        page.setContent(content);
        return page;
    }
}
