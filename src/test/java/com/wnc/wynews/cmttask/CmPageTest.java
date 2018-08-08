
package com.wnc.wynews.cmttask;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.wnc.basic.BasicFileUtil;
import com.wnc.string.PatternUtil;
import com.wnc.tools.FileOp;
import com.wnc.wynews.helper.WySpiderClient;
import com.wnc.wynews.model.NewsModule;
import com.wnc.wynews.spy.WyCmtTask;
import com.wnc.wynews.utils.ProxyUtil;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.crawl.spider.task.AbstractPageTask;

public class CmPageTest {
    Set set = new HashSet<String>();

    @Test
    public void a() throws Exception {
        AbstractPageTask.retryMap.put(WyCmtTask.class,
                new ConcurrentHashMap<String, Integer>());

        new ProxyUtil().initProxyPool();

        List<String> lines = FileOp.readFrom("hot-news-0725.txt");
        for (String line : lines) {
            String code = PatternUtil.getLastPatternGroup(line, ".+/(.*?)\\.html");
            if (StringUtils.isNotBlank(code) && code.length() > 10 && set.add(code)) {
                WySpiderClient.getInstance().submitTask(new WyCmtTask(new NewsModule("HOT", "2000-01-01 00:00:00"), code, 0));
            }
        }
        while (true) {
            Thread.sleep(10000);
        }
    }

    @Test
    public void as(){
        BasicFileUtil.renameFile("c:/data/1.txt.bak", "c:/data/1.txt");
    }
}
