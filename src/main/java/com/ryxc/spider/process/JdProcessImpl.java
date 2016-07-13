package com.ryxc.spider.process;

import com.ryxc.spider.domain.Page;
import com.ryxc.spider.utils.PageUtils;
import com.ryxc.spider.utils.RevUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tonye0115 on 2016/7/1.
 */
public class JdProcessImpl implements Processable {

    Logger logger = LoggerFactory.getLogger(JdProcessImpl.class);
    /**
     * 获取下一页 xpath
     * //*[@id="J_topPage"]/a[2]
     * 获取当前页所有商品url  xpath
     * //*[@id="plist"]/ul/li/div/div[1]/a
     *
     * @param page
     */
    public void process(Page page) {
        HtmlCleaner htmlCleaner = new HtmlCleaner();
        TagNode rootNode = htmlCleaner.clean(page.getContent());
        if (page.getUrl().startsWith("http://item.jd.com")) {
            parseProduct(page, rootNode);
        } else {
            //解析列表页面
            try {
                //获取下一页
                Object[] nextUrlEvaluateXPath = rootNode.evaluateXPath("//*[@id=\"J_topPage\"]/a[2]");
                if (nextUrlEvaluateXPath != null && nextUrlEvaluateXPath.length > 0) {
                    TagNode nextUrlNode = (TagNode) nextUrlEvaluateXPath[0];
                    String nextUrl = nextUrlNode.getAttributeByName("href");
                    if(!nextUrl.equals("javascript:;")){
                        page.addUrl("http://list.jd.com/"+nextUrl);
                    }
                }

                //获取当前页面的所有url
                Object[] aEvaluateXPath = rootNode.evaluateXPath("//*[@id=\"plist\"]/ul/li/div/div[1]/a");
                for (Object object:aEvaluateXPath
                     ) {
                    TagNode aNode = (TagNode)object;
                    page.addUrl("http:"+aNode.getAttributeByName("href"));
                }

            } catch (XPatherException e) {
                e.printStackTrace();
            }
        }

    }



    /**
     * 解析单个商品
     * @param page
     * @param rootNode
     */
    public void parseProduct(Page page,TagNode rootNode){
        try {

            //获取标题
            Object[] titileEvelauateXpath = rootNode.evaluateXPath("//div[@class='product-intro clearfix']/div[2]/div[1]");
            if (titileEvelauateXpath != null && titileEvelauateXpath.length > 0) {
                TagNode titileNode = (TagNode) titileEvelauateXpath[0];
                page.addField("title",titileNode.getText().toString());
                logger.info("title:"+titileNode.getText().toString());
            }


            //获取图片
            ///html/body/div[5]/div/div[1]/div/div[1]/img
           // Object[] imgUrlEvaluateXPath = rootNode.evaluateXPath("//*[@id=\"spec-n1\"]/img");
            Object[] imgUrlEvaluateXPath = rootNode.evaluateXPath("//div[@class='product-intro clearfix']/div[1]/div/div[1]/img[1]");

            if (imgUrlEvaluateXPath != null && imgUrlEvaluateXPath.length > 0) {
                TagNode imgUrlNode = (TagNode) imgUrlEvaluateXPath[0];
                String imgUrl = imgUrlNode.getAttributeByName("data-origin");
                page.addField("picurl","http:" + imgUrl);
                //logger.info("imgUrl:"+imgUrl);
            }


            //获取价格
            Pattern pattern = Pattern.compile("http://item.jd.com/([0-9]+).html");
            String goodsId = "";
            Matcher matcher = pattern.matcher(page.getUrl());
            if (matcher.find()) {
                goodsId = matcher.group(1);
            }
            page.setGoodsId(RevUtils.reverse(goodsId)+"_jd");

            //http://p.3.cn/prices/get?skuid=J_
            //p.3.cn/prices/mgets?pduid=774241501&pdpin=&pdbp=0&skuIds=J_10435042985
            String content = PageUtils.getContent("http://p.3.cn/prices/mgets?pduid=774241501&pdpin=&pdbp=0&skuIds=J_" + goodsId);
            //System.out.println(content);
            JSONArray jsonArray = new JSONArray(content);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String price = jsonObject.getString("p");
            page.addField("price",price);


            //获取规格参数
            JSONArray specJsonArray = new JSONArray();

       /*    //*[@id="product-detail-2"]/table/tbody/tr[1]/th
            Object[] trEvaluateXPath = rootNode.evaluateXPath("/*//*[@id=\"product-detail-2\"]/table/tbody/tr");
            if (trEvaluateXPath != null && trEvaluateXPath.length > 0) {
                for (Object object : trEvaluateXPath
                        ) {
                    TagNode trNode = (TagNode) object;
                    //过滤非空的tr
                    if (!trNode.getText().toString().trim().equals("")) {
                        JSONObject jsonObjectTh = new JSONObject();
                        //判断是否为th
                        Object[] thEvaluateXPath = trNode.evaluateXPath("//th");
                        if (thEvaluateXPath != null && thEvaluateXPath.length > 0) {
                            TagNode thNode = (TagNode) thEvaluateXPath[0];
                            jsonObjectTh.put("name", "");
                            jsonObjectTh.put("name", "");
                            jsonObjectTh.put("value", thNode.getText());
                        } else {
                            //获取td
                            Object[] tdEvaluateXPath = trNode.evaluateXPath("//td");
                            TagNode tdNode1 = (TagNode) tdEvaluateXPath[0];
                            TagNode tdNode2 = (TagNode) tdEvaluateXPath[1];
                            jsonObjectTh.put("name", tdNode1.getText());
                            jsonObjectTh.put("name", tdNode1.getText());
                            jsonObjectTh.put("value", tdNode2.getText());

                        }
                        specJsonArray.put(jsonObjectTh);
                    }
                }
            }*/


            //*[@id="detail"]/div[2]/div[2]/div[2]
            Object[] divEvaluateXPath = rootNode.evaluateXPath("//*[@id=\"detail\"]/div[2]/div[2]/div[2]");
            if (divEvaluateXPath != null && divEvaluateXPath.length > 0) {
                for (Object object : divEvaluateXPath
                        ) {
                    TagNode divNode = (TagNode) object;
                    Object[] dlEvaluateXPath = divNode.evaluateXPath("//dl");

                    for (Object dlobject : dlEvaluateXPath
                            ) {
                        TagNode dlNode = (TagNode) dlobject;
                        Object[] dtEvaluateXPath = dlNode.evaluateXPath("//dt");
                        TagNode dtNode1 = (TagNode) dtEvaluateXPath[0];
                        Object[] ddEvaluateXPath = dlNode.evaluateXPath("//dd");
                        TagNode ddNode1 = (TagNode) ddEvaluateXPath[0];
                        //logger.info(dtNode1.getText() + " : " + ddNode1.getText());
                        JSONObject jsonObjectdl = new JSONObject();
                        jsonObjectdl.put("name", dtNode1.getText());
                        jsonObjectdl.put("value", ddNode1.getText());
                        specJsonArray.put(jsonObjectdl);
                        //logger.info("specJsonArray:"+specJsonArray.toString());
                    }

                }
            }

            page.addField("spec",specJsonArray.toString());
            //logger.info("page:"+page.getFieldsMap());
        } catch (XPatherException e) {
            e.printStackTrace();
        }
    }
}
