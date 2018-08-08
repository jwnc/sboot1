package com.wnc.wynews.news;

import com.alibaba.fastjson.JSONObject;

import com.wnc.basic.BasicFileUtil;
import com.wnc.string.PatternUtil;
import com.wnc.tools.FileOp;
import com.wnc.wynews.consts.WyConsts;
import com.wnc.wynews.model.News;
import com.wnc.wynews.parser.*;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.io.File;
import java.util.*;

/**
 * @Description 从新闻源码中解析出新闻列表
 * @Author nengcai.wang
 * @Date 2018/7/28 10:46
*/
public class SeekNewsList {

    Set<String> set = new HashSet<String>();
    int count = 0;

    @Test
    public void seekNews() throws Exception {
        String folder = WyConsts.NEWS_MENU_DIR + "2018-07-26\\";
        for (File f : new File(folder).listFiles()) {
            String module = PatternUtil.getFirstPatternGroup(f.getName(), "(.*?)\\-");
            List urls = readNews(f);
            count += urls.size();

            for (Object obj : urls) {
                BasicFileUtil.writeFileString(WyConsts.NEWS_LIST_DIR + module+".txt", JSONObject.toJSONString(obj) + "\r\n", null, true);
            }
        }
        System.out.println(count);
    }

    /***
     * @Description 从已经下载好的txt中解析url连接
     * @Date 2018/7/26 16:38
     * @Param f
     * @Return java.util.List<java.lang.String>
     */
    private List<? extends Object> readNews(File f) {
        String join = StringUtils.join(FileOp.readFrom(f.getAbsolutePath(), "UTF-8"), "");
        List<? extends Object> ret = new ArrayList<Object>();
        if (org.apache.commons.lang3.StringUtils.containsAny(f.getName(), "IT", "互联网")) {
            ret = parseTechHtml(join);
        } else if (org.apache.commons.lang3.StringUtils.containsAny(f.getName(), "新车", "试驾", "导购", "用车", "行业")) {
            ret = parseAutoHtml(join);
        } else if (f.getName().contains("数读")) {
            ret = parseDataHtml(join);
        } else {
            ret = parseJson(join);
        }
        return ret;
    }

    private List<News> parseJson(String join) {
        return new WyJsonParser(join).getNewsList();
    }

    private List parseTechHtml(String join) {
        return new WyTechParser(join).getNewsList();
    }

    private List parseAutoHtml(String join) {
        return new WyAutoParser(join).getNewsList();
    }

    private List parseDataHtml(String join) {
        return new WyDataParser(join).getNewsList();
    }
}
