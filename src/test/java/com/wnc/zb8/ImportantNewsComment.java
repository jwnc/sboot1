package com.wnc.zb8;

import com.alibaba.fastjson.JSONArray;
import com.wnc.basic.BasicFileUtil;
import com.wnc.basic.BasicNumberUtil;
import com.wnc.sboot1.spy.zuqiu.HotComment;
import com.wnc.string.PatternUtil;
import org.junit.Test;
import translate.util.JsoupHelper;

import java.util.List;

public class ImportantNewsComment {
    @Test
    public void lady2018() throws Exception {
        String newsUrl = "https://news.zhibo8.cc/zuqiu/2018-12-26/5c21e564e6e68.htm";
        final String title = "回顾2018：花色足坛众生相，燃烧你的卡路里！";
        final String headUrl = "https://cache.zhibo8.cc/json/2018_12_26/news/zuqiu/5c21e564e6e68_";
        getAllComments(title, headUrl);
    }

    private void getAllComments(String title, String headUrl) throws Exception {
        String jsonResult = JsoupHelper.getJsonResult(headUrl + "count.htm?key=" + Math.random());
        String firstPatternGroup = PatternUtil.getFirstPatternGroup(jsonResult, "\"root_normal_num\":\\s*?(\\d+)");
        int pages = BasicNumberUtil.getNumber(firstPatternGroup) / 99;
        for (int i = 0; i <= pages; i++) {
            Thread.sleep(1000L);
            jsonResult = JsoupHelper.getJsonResult(headUrl + i + ".htm?key=" + Math.random());
            List<HotComment> hotComments = JSONArray.parseArray(jsonResult, HotComment.class);
            for (HotComment cmt : hotComments) {
                BasicFileUtil.writeFileString(title + ".txt", cmt.getContent() + "\r\n", null, true);
            }
        }
    }
}
