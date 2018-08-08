
package com.wnc.wynews.bulk;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawl.spider.task.AbstractPageTask;
import com.wnc.string.PatternUtil;
import com.wnc.tools.FileOp;
import com.wnc.wynews.consts.UserManager;
import com.wnc.wynews.consts.WyConsts;
import com.wnc.wynews.helper.WySpiderClient;
import com.wnc.wynews.model.DataNews;
import com.wnc.wynews.model.News;
import com.wnc.wynews.model.NewsModule;
import com.wnc.wynews.spy.WyCmtTask;
import com.wnc.wynews.utils.ProxyUtil;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/***
 * @Description 加载今日之前的历史数据
 * @Date 2018/7/26 12:00
 * @Param null
 * @Return
 */
public class CommentBulkTest_Add {
    Set<String> set = new HashSet<String>();
    int count = 0;

    @Test
    public void bulkCmts() throws Exception {
        UserManager.initUsers();

        AbstractPageTask.retryMap.put(WyCmtTask.class,
                new ConcurrentHashMap<String, Integer>());

        new ProxyUtil().initProxyPool();

        String folder = WyConsts.NEWS_MENU_DIR + "2018-07-26-add\\";
        for (File f : new File(folder).listFiles()) {
            List<String> urls = readUrls(f);
            count += urls.size();

            for (String url : urls) {
                String code = PatternUtil.getLastPatternGroup(url, ".+/(.*?)\\.html");
                System.out.println("url " + url + " code:" + code);
                if (StringUtils.isNotBlank(code) && code.length() > 10 && set.add(code)) {
                    String name = PatternUtil.getFirstPatternGroup(f.getName(), "(.*?)\\.");
                    WySpiderClient.getInstance().submitTask(new WyCmtTask(new NewsModule(name, "2000-01-01 00:00:00"), code, 0));
                }
            }
        }
        System.out.println(count);
        while (true) {
            Thread.sleep(10000);
        }
    }

    /***
     * @Description 从已经下载好的txt中解析url连接
     * @Date 2018/7/26 16:38
     * @Param f
     * @Return java.util.List<java.lang.String>
     */
    private List<String> readUrls(File f) {
        String join = StringUtils.join(FileOp.readFrom(f.getAbsolutePath(), "UTF-8"), "");
        List<String> ret = new ArrayList<String>();
        if (org.apache.commons.lang3.StringUtils.containsAny(f.getName(), "IT", "互联网")) {
            ret = parseTechHtml(join);
        } else if (org.apache.commons.lang3.StringUtils.containsAny(f.getName(), "新车", "试驾", "导购", "用车", "行业")) {
            ret = parseAutoHtml(join);
        } else if (f.getName().contains("数读")) {
            ret = parseDataHtml(join);
        } else {
//            ret = parseJson(join);
        }
        return ret;
    }

    private List<String> parseJson(String join) {
        List<String> ret = new ArrayList<String>();
        String listStr = join.replaceAll("data_callback\\(|\\)$", "");
        List<News> news = JSONArray.parseArray(listStr, News.class);
        for (News n : news) {
//            System.out.println(n.getTitle() + " / " + n.getTime());
            ret.add(n.getCommenturl());
        }
        return ret;
    }

    private List<String> parseTechHtml(String join) {
        List<String> ret = new ArrayList<String>();
        Document doc = Jsoup.parse(join);
        Elements select = doc.select("#news-flow-content li");
        for (Element e : select) {
            ret.add(e.select("a.commentCount").attr("href"));
        }
        return ret;
    }

    private List<String> parseAutoHtml(String join) {
        List<String> ret = new ArrayList<String>();
        Document doc = Jsoup.parse(join);
        Elements select = doc.select("#auto_pull_dataset .auto_news_item");
        for (Element e : select) {
            ret.add(e.select("div.item-cont > h3 > a").attr("href"));
        }
        return ret;
    }

    private List<String> parseDataHtml(String join) {
        List<String> ret = new ArrayList<String>();
        String firstPatternGroup = PatternUtil.getFirstPatternGroup(join.replace("\n", ""), " ohnofuchlist=(\\[.*?\\]);");
//        System.out.println(firstPatternGroup);
        List<String> allPatternGroup = PatternUtil.getAllPatternGroup(firstPatternGroup, "(\\{.*?\\})");
        for (String s : allPatternGroup) {
            DataNews dataNews = JSONObject.parseObject(s.replaceAll("'", "\""), DataNews.class);
            ret.add(dataNews.getUrl());
        }
        return ret;
    }
}
