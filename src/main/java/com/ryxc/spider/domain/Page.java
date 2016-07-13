package com.ryxc.spider.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tonye0115 on 2016/7/1.
 */
public class Page {

    private String goodsId;

    private String content;

    private String url;

    private Map<String,String> fieldsMap = new HashMap<String,String>();

    //临时保存url
    private List urls = new ArrayList<String>();

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void addField(String key, String value) {
        this.fieldsMap.put(key, value);
    }

    public Map<String, String> getFieldsMap() {
        return fieldsMap;
    }

    public void addUrl(String url){
        this.urls.add(url);
    }

    public List getUrls() {
        return urls;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }


}
