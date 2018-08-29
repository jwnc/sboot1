
package com.wnc.wynews.spy;

import java.util.Set;

import com.wnc.wynews.jpa.EntityConvertor;
import com.wnc.wynews.service.WyDbService;
import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.crawl.spider.entity.Page;
import com.crawl.spider.task.AbstractPageTask;
import com.wnc.basic.BasicFileUtil;
import com.wnc.wynews.consts.UserManager;
import com.wnc.wynews.helper.WySpiderClient;
import com.wnc.wynews.model.Comment;
import com.wnc.wynews.model.NewsModule;
import com.wnc.wynews.model.User;
import com.wnc.wynews.utils.WyNewsUtil;

/**
 * 找出两个月之间的
 */
public class WyCmtTask extends AbstractPageTask {
    private static String CMT_URL_FORMAT = "http://comment.api.163.com/api/v1/products/a2869674571f77b5a0867c3d71db5856/"
            + "threads/%s/comments/newList?ibc=newspc&limit=30&showLevelThreshold=72&headLimit=1&tailLimit=2&offset=%d";
    private static Logger logger = Logger.getLogger(WyCmtTask.class);
    private String code;
    private int offset;
    NewsModule newsModule;
    private int cmtListSize;
    private final static int PAGE_SIZE = 30;

    private boolean ignoreComp = false;

    private WyDbService wyDbService = WyNewsUtil.getWyDbService();

    public WyCmtTask(NewsModule newsModule, String code, int offset) {
        this.newsModule = newsModule;
        this.code = code;
        this.offset = offset;

        this.url = String.format(CMT_URL_FORMAT, this.code, this.offset);
        this.proxyFlag = true;

        this.MAX_RETRY_TIMES = 20;

        request = new HttpGet(url);
    }

    public WyCmtTask setMaxRetryTimes(int tm) {
        this.MAX_RETRY_TIMES = tm;
        return this;
    }

    @Override
    public void run() {
        super.run();
    }

    @Override
    protected void retry() {
        WySpiderClient.getInstance()
                .submitTask(new WyCmtTask(newsModule, code, offset));
    }

    @Override
    protected void handle(Page page) throws Exception {
        String res = page.getHtml();
        cmtListSize = JSONObject.parseObject(res)
                .getIntValue("newListSize");
        if (cmtListSize > 0) {
            outputCmtData(res);
            if (cmtListSize > offset + PAGE_SIZE) {
                nextJob();
                ignoreComp = true;
            }
        } else {
            // 无记录, 记作错误日志
            complete(99, "评论列表空白");
            ignoreComp = true;
        }

    }

    private void outputCmtData(String res) {
        System.out.println("OFFSET:" + offset + " " + url);

        JSONObject jsonObject = JSONObject.parseObject(res);
        JSONObject commentsJO = jsonObject.getJSONObject("comments");
        Set<String> keys = commentsJO.keySet();
        for (String key : keys) {
            JSONObject cmtJO = commentsJO.getJSONObject(key);
            Comment cmt = cmtJO.toJavaObject(Comment.class);
            User user = cmt.getUser();
            cmt.setUser(null);
            cmt.setUserId(user.getUserId());
            // 输出cmt到curDateStr目录
            BasicFileUtil.writeFileString(
                    WyNewsUtil.getCommentLocation(newsModule, code),
                    JSONObject.toJSONString(cmt) + "\r\n", null, true);
            if (!cmt.isAnonymous()) {
                System.out.println(
                        user.getNickname() + " / " + user.getUserId());
                // 输出user到user目录
                UserManager.addAndWriteUser(user);
                // 插入用户到数据库
                wyDbService.singleUser(EntityConvertor.userToEntity(user));
            }
        }

    }

    private void nextJob() {
        offset += PAGE_SIZE;
        WySpiderClient.getInstance()
                .submitTask(new WyCmtTask(newsModule, code, offset));
    }

    // 404还要继续
    protected void errLog404(Page page) {
        ignoreComp = true;
        retryMonitor("404 continue...");
    }

    @Override
    protected void complete(int type, String msg) {
        if (ignoreComp) {
            return;
        }
        try {
            super.complete(type, msg);

            if (type != COMPLETE_STATUS_SUCCESS) {
                logger.error(code + "在" + url + "失败, 失败原因:" + msg);
                WyNewsUtil.err(newsModule, code + "在" + url + "失败, 失败原因:" + msg
                        + " (" + offset + "/" + cmtListSize + ")");
            } else {
                // 任务完成数加1
                WySpiderClient.getInstance().counterTaskComp();
                WyNewsUtil.log(newsModule.getName() + "\\" + code
                        + "成功!抓取评论总数:" + cmtListSize);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            WyNewsUtil.recoverCmtFileAfterTask(this.newsModule, code);
        }
    }
}
