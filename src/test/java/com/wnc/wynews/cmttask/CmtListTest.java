package com.wnc.wynews.cmttask;

import com.alibaba.fastjson.JSONObject;
import com.wnc.basic.BasicFileUtil;
import com.wnc.string.PatternUtil;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import translate.util.JsoupHelper;

public class CmtListTest {
    final int PAGE_SIZE = 30;

    @Test
    public void a() throws Exception {
        printComment("DNIK4VOV00018AOR");
    }

    private void printComment(String code) throws Exception {
        int offset = 0;
        String res = JsoupHelper.getJsonResult("http://comment.api.163.com/api/v1/products/a2869674571f77b5a0867c3d71db5856/threads/" + code + "/comments/newList?ibc=newspc&limit=30&showLevelThreshold=72&headLimit=1&tailLimit=2&offset=" + offset);
        System.out.println("OFFSET:" + offset);
        BasicFileUtil.writeFileString("cmt-" + code + ".txt", res + "\r\n", null, true);

        int newListSize = JSONObject.parseObject(res).getIntValue("newListSize");
        System.out.println(newListSize);
        while (newListSize > offset + PAGE_SIZE) {
            offset += PAGE_SIZE;
            res = JsoupHelper.getJsonResult("http://comment.api.163.com/api/v1/products/a2869674571f77b5a0867c3d71db5856/threads/" + code + "/comments/newList?ibc=newspc&limit=30&showLevelThreshold=72&headLimit=1&tailLimit=2&offset=" + offset);
            System.out.println("OFFSET:" + offset);
            BasicFileUtil.writeFileString("cmt-" + code + ".txt", res + "\r\n", null, true);
        }
    }

    @Test
    public void ab() throws Exception {
        Document doc = JsoupHelper.getDocumentResult("http://news.163.com/rank/");
//        System.out.println(doc);
        Elements elements = doc.select(".tabContents td > a");
        System.out.println(elements.size());

        for (int i = 0; i < elements.size(); i++) {
            String href = elements.get(i).attr("href");
            String text = elements.get(i).text();
            BasicFileUtil.writeFileString("hot-news-0725.txt", text+" "+href + "\r\n", null, true);
            System.out.println(text + " " + href);
            //http://comment.tie.163.com/DNHSRJU6001081EI.html
            String code = PatternUtil.getLastPatternGroup(href, ".+/(.*?)\\.html");
//            System.out.println("http://comment.tie.163.com/" + code);
//            printComment(code);
        }
    }
}
