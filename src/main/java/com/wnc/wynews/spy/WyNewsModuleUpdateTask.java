
package com.wnc.wynews.spy;

import com.crawl.spider.entity.Page;
import com.crawl.spider.task.AbstractPageTask;
import com.wnc.basic.BasicDateUtil;
import com.wnc.wynews.helper.WySpiderClient;
import com.wnc.wynews.jpa.EntityConvertor;
import com.wnc.wynews.jpa.entity.WyNews;
import com.wnc.wynews.model.News;
import com.wnc.wynews.model.NewsModule;
import com.wnc.wynews.parser.WyParser;
import com.wnc.wynews.utils.NewsPageUrlGenerator;
import com.wnc.wynews.utils.WyNewsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 项目初始化, 拉取之前的新闻数据到本地 2018-07-26 12:00:00
 */
public class WyNewsModuleUpdateTask extends WyNewsModuleTask {

    public WyNewsModuleUpdateTask(NewsModule newsModule, int pageIdx) {
        super(newsModule, pageIdx, null);
    }

    @Override
    protected void retry() {
        WySpiderClient.getInstance().submitTask(new WyNewsModuleUpdateTask(
                this.newsModule, pageIdx));
    }

    @Override
    protected void handle(Page page) throws Exception {
        if (page.getHtml().contains("<title>Index_Page</title>")) {
            // 重试
            retryMonitor("Page Error ...");
            ignoreComp = true;
        } else {
            WyParser parser = WyNewsUtil.getNewsParser(this.newsModule,
                    page.getHtml());
            List<News> list = parser.getNewsList();
            if (!newsModule.isOnePageModule()) {
                // 下一页任务
                nextJob();
                ignoreComp = true;
            }
            // 处理当前页的数据
            dealCurrentPageNews(list);
            WyNewsUtil.log("更新" + newsModule.getName() + "任务完成-" + pageIdx);
        }

    }

    private void dealCurrentPageNews(List<News> list) {
        if (!list.isEmpty()) {
            for (News news : list) {
                // 更新新闻, 暂时只更新评论数
                WyNews wyNews = EntityConvertor.newsToEntity(newsModule.getName(), news);
                wyDbService.updateNews(wyNews);
            }
        }
    }
    private void nextJob() {
        int t_page = pageIdx + 1;
        WySpiderClient.getInstance().submitTask(new WyNewsModuleUpdateTask(
                this.newsModule, t_page));
    }

    @Override
    protected void complete(int type, String msg) {
        if (ignoreComp) {
            return;
        }
        try {
            super.complete(type, msg);

            String name = newsModule.getName();

            if (type != COMPLETE_STATUS_SUCCESS) {
                WyNewsUtil.err(newsModule,
                        name + "在" + url + "失败, 失败原因:" + msg);
            } else {
                // 任务完成数加1
                WySpiderClient.getInstance().counterTaskComp();
                WyNewsUtil.log("更新任务成功:" + name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

}
