package com.ryxc.spider.process;

import com.ryxc.spider.domain.Page;
import com.ryxc.spider.utils.PageUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tonye0115 on 2016/7/1.
 */
public class JdProcessImpl implements Processable {
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
                        page.addUrl("http:/"+nextUrl);
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
            Object[] titileEvelauateXpath = rootNode.evaluateXPath("//*[@id=\"name\"]/h1");
            if (titileEvelauateXpath != null && titileEvelauateXpath.length > 0) {
                TagNode titileNode = (TagNode) titileEvelauateXpath[0];
                page.addField("title",titileNode.getText().toString());
            }


            //获取图片
            Object[] imgUrlEvaluateXPath = rootNode.evaluateXPath("//*[@id=\"spec-n1\"]/img");
            if (imgUrlEvaluateXPath != null && imgUrlEvaluateXPath.length > 0) {
                TagNode imgUrlNode = (TagNode) imgUrlEvaluateXPath[0];
                String imgUrl = imgUrlNode.getAttributeByName("src");
                page.addField("imgUrl","http:" + imgUrl);
            }


            //获取价格
            Pattern pattern = Pattern.compile("http://item.jd.com/([0-9]+).html");
            String goodsId = "";
            Matcher matcher = pattern.matcher(page.getUrl());
            if (matcher.find()) {
                goodsId = matcher.group(1);
            }

            String content = PageUtils.getContent("http://p.3.cn/prices/get?skuid=J_" + goodsId);
            System.out.println(content);
            JSONArray jsonArray = new JSONArray(content);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String price = jsonObject.getString("p");
            page.addField("price",price);


            //获取规格参数  //*[@id="product-detail-2"]/table/tbody/tr[1]/th
            JSONArray specJsonArray = new JSONArray();

            Object[] trEvaluateXPath = rootNode.evaluateXPath("//*[@id=\"product-detail-2\"]/table/tbody/tr");
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
            }

            page.addField("spec",specJsonArray.toString());

        } catch (XPatherException e) {
            e.printStackTrace();
        }
    }
}
