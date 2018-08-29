
package com.wnc.sboot1.spy.wy;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.stereotype.Component;

import com.crawl.spider.task.AbstractPageTask;
import com.wnc.basic.BasicFileUtil;
import com.wnc.sboot1.spy.util.SpiderUtils;
import com.wnc.string.PatternUtil;
import com.wnc.tools.FileOp;
import com.wnc.wynews.consts.WyConsts;
import com.wnc.wynews.helper.WySpiderClient;
import com.wnc.wynews.model.NewsModule;
import com.wnc.wynews.spy.WyCmtTask;
import com.wnc.wynews.utils.ProxyUtil;
import com.wnc.wynews.utils.WyNewsUtil;

@Component
public class WyNewsDaySpy {
    public void spy() throws IOException, InterruptedException {
        WySpiderClient.getInstance().counterReset();
        String yesterday = SpiderUtils.getYesterDayStr();
        String newsFile = WyConsts.NEWS_DAY_DIR + yesterday + ".txt";
        if (BasicFileUtil.isExistFile(newsFile)) {
            WyNewsUtil.log("WyNewsDaySpy任务启动");
            long startTime = System.currentTimeMillis();
            AbstractPageTask.retryMap.put(WyCmtTask.class,
                    new ConcurrentHashMap<String, Integer>());

            new ProxyUtil().initProxyPool();

            List<String> lines = FileOp.readFrom(newsFile, "UTF-8");
            Set<String> set = new HashSet<String>(lines);
            for (String line : set) {
                String moduleName = PatternUtil.getFirstPatternGroup(line,
                        "\\[(.*?)\\]");
                NewsModule newsModule = new NewsModule(moduleName,
                        new Date());
                String code = PatternUtil.getFirstPatternGroup(line,
                        "(.*?)\\[");
                if (WyNewsUtil.isValidCode(code)) {
                    WyNewsUtil.backupCmtFileBeforeTask(newsModule, code);
                    WySpiderClient.getInstance()
                            .submitTask(new WyCmtTask(newsModule, code, 0));
                }
            }
            ThreadPoolExecutor netPageThreadPool = WySpiderClient.getInstance().getNetPageThreadPool();
            while (netPageThreadPool.getActiveCount() > 0 || netPageThreadPool.getQueue().size() > 0) {
                Thread.sleep(10000);
            }

            WyNewsUtil.log("WyNewsDaySpy任务成功完成. 完成子任务数:"
                    + WySpiderClient.parseCount + "任务耗时:"
                    + (System.currentTimeMillis() - startTime) / 1000 + "秒.");

        }

    }

}