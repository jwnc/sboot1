
package com.wnc.wynews.bulk;

import com.alibaba.fastjson.JSONObject;
import com.crawl.spider.task.AbstractPageTask;
import com.wnc.tools.FileOp;
import com.wnc.wynews.consts.WyConsts;
import com.wnc.wynews.helper.WySpiderClient;
import com.wnc.wynews.model.NewsModule;
import com.wnc.wynews.spy.WyNewsModuleTask;
import com.wnc.wynews.utils.ProxyUtil;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import java.util.Date;
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
public class NewsPageBulkTest {
    Set<NewsModule> set = new HashSet<NewsModule>();

    @Test
    public void a() throws Exception {
        module();

        AbstractPageTask.retryMap.put(WyNewsModuleTask.class,
                new ConcurrentHashMap<String, Integer>());

        new ProxyUtil().initProxyPool();

        for (NewsModule newsModule : set) {
            if(newsModule.getIgnore()){
                continue;
            }
            WySpiderClient.getInstance().submitTask(new WyNewsModuleTask(newsModule, 1, new Date()));
        }


        while (true) {
            Thread.sleep(10000);
        }
    }

    public void module() {
        List<String> list = FileOp.readFrom(WyConsts.WYNEWS_CONF);
        NewsModule module = JSONObject.parseObject(StringUtils.join(list, ""), NewsModule.class);
        System.out.println(module);
        printLeaf(module);
    }

    private void printLeaf(NewsModule newsModule) {
        if (CollectionUtils.isEmpty(newsModule.getNodes())) {
            if(!newsModule.getIgnore()) {
                System.out.println(newsModule.getName());
                set.add(newsModule);
            }
        } else {
            for (NewsModule newsModule2 : newsModule.getNodes()) {
                printLeaf(newsModule2);
            }
        }
    }
}
