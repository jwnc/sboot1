
package com.wnc.wynews.utils;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.wnc.sboot1.SpringContextUtils;
import com.wnc.wynews.service.WyDbService;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.wnc.basic.BasicDateUtil;
import com.wnc.basic.BasicFileUtil;
import com.wnc.string.PatternUtil;
import com.wnc.tools.FileOp;
import com.wnc.wynews.consts.WyConsts;
import com.wnc.wynews.model.News;
import com.wnc.wynews.model.NewsModule;
import com.wnc.wynews.parser.WyAutoParser;
import com.wnc.wynews.parser.WyDataParser;
import com.wnc.wynews.parser.WyJsonParser;
import com.wnc.wynews.parser.WyParser;
import com.wnc.wynews.parser.WyTechParser;

public class WyNewsUtil {
    /***
     * 手动获取数据库服务bean
     * @since 2018/8/29 11:17
     * @param  
     * @return com.wnc.wynews.service.WyDbService
    */
    public static WyDbService getWyDbService() {
        return (WyDbService) SpringContextUtils.getContext().getBean("wyDbService");
    }

    /***
     * @Description 获取新闻code, 评论页面必须要用到
     * @Date 2018/7/27 17:59
     * @Param news
     * @Return java.lang.String
     */
    public static String getNewsCode(News news) {
        String url = "";
        if (StringUtils.isNotBlank(news.getCommenturl())) {
            url = news.getCommenturl();
        } else if (StringUtils.isNotBlank(news.getDocurl())) {
            url = news.getDocurl();
        }
        String code = "";
        if (url.endsWith(".html")) {
            code = PatternUtil.getLastPatternGroup(url, ".+/(.*?)\\.");
        } else {
            code = PatternUtil.getLastPatternGroup(url, ".+/(.*?)$");
        }

        if (code.length() < 10) {
            if (url.contains("photoview")) {
                // 相册新闻的code可以从img中取
                // http://auto.163.com/photoview/2O3E0008/198668.html
                // DLO9FHKS2O3E0008NOS
                if (StringUtils.isNotBlank(news.getImgurl())) {
                    url = news.getImgurl();
                }
                code = PatternUtil.getLastPatternGroup(url, ".+/(.*?)\\.");
            }
        }
        return code;
    }

    /**
     * @Description 获取评论保存地址
     * @Date 2018/7/27 18:08
     * @Param module
     * @Param news
     * @Return java.lang.String
     */
    public static String getCommentLocation(NewsModule module, String code) {
        String folder = WyConsts.COMMENTS_DIR + module.getName();
        if (!BasicFileUtil.isExistFolder(folder)) {
            BasicFileUtil.makeDirectory(folder);
        }
        return folder + "\\cmt-" + code + ".txt";
    }

    /****
     * @Description 获取统一格式的时间 数读.txt: [2018-07-22 21:06:25] 新车.txt: [2018-07-26]
     *              旅游.txt: [07/26/2018 01:19:01]
     */
    public static String getFormatTimeStr(String timeStr) {
        final String defaultTimeStr = "2018-07-26 12:00:00";
        if (StringUtils.isNotBlank(timeStr)) {
            SimpleDateFormat sdf = new SimpleDateFormat();
            if (timeStr.contains("/")) {
                return BasicDateUtil.getLocalDataFromFormatString(timeStr,
                        "MM/dd/yyyy HH:mm:ss");
            } else if (timeStr.contains(":")) {
                return BasicDateUtil.getLocalDataFromFormatString(timeStr,
                        "yyyy-MM-dd HH:mm:ss");
            } else if (timeStr.length() == 10) {
                return BasicDateUtil.getLocalDataFromFormatString(timeStr,
                        "yyyy-MM-dd");
            }
        }
        return defaultTimeStr;
    }

    private static Set<NewsModule> set = new HashSet<NewsModule>();

    public static Set<NewsModule> getNewsModules() {
        if (set.size() == 0) {
            module();
        }
        return set;
    }

    private static void module() {
        List<String> list = FileOp.readFrom(WyConsts.WYNEWS_CONF);
        NewsModule module = JSONObject
                .parseObject(StringUtils.join(list, ""), NewsModule.class);
        System.out.println(module);
        printLeaf(module);
    }

    private static void printLeaf(NewsModule newsModule) {
        if (CollectionUtils.isEmpty(newsModule.getNodes())) {
            if (!newsModule.getIgnore()) {
                System.out.println(newsModule.getName());
                set.add(newsModule);
            }
        } else {
            for (NewsModule newsModule2 : newsModule.getNodes()) {
                printLeaf(newsModule2);
            }
        }
    }

    public static WyParser getNewsParser(NewsModule module, String html) {
        String name = module.getName();
        String url = module.getUrl();
        if (name.equals("IT") || name.equals("互联网")) {
            return new WyTechParser(html);
        } else if (url.startsWith("http://auto.163.com")) {
            return new WyAutoParser(html);
        } else if (url.startsWith("http://data.163.com")) {
            return new WyDataParser(html);
        }
        return new WyJsonParser(html);
    }

    public static void log(String msg) {
        String line = BasicDateUtil.getCurrentDateTimeString() + " " + msg
                + "\r\n";
        BasicFileUtil.writeFileString(WyConsts.LOG_TXT, line, null, true);
    }

    public static void err(NewsModule newsModule, String msg) {
        String line = BasicDateUtil.getCurrentDateTimeString() + " "
                + newsModule.getName() + "{ " + msg + "}\r\n";
        BasicFileUtil.writeFileString(WyConsts.ERR_TXT, line, null, true);
    }

    /**
     * 抓评论之前备份文件
     *
     * @param newsModule
     * @param code
     */
    public static void backupCmtFileBeforeTask(NewsModule newsModule,
                                               String code) {
        // 处理评论文件, 原有的做备份
        String cmtPath = WyNewsUtil.getCommentLocation(newsModule, code);
        String cmtBackPath = cmtPath + ".bak";
        if (BasicFileUtil.isExistFile(cmtBackPath)) {
            WyNewsUtil.log("WyNewsModuleTask删除备份文件:"
                    + cmtBackPath.replace(WyConsts.COMMENTS_DIR, ""));
            BasicFileUtil.deleteFile(cmtBackPath);
        }
        if (BasicFileUtil.isExistFile(cmtPath)) {
            WyNewsUtil.log("WyNewsModuleTask创建备份文件:"
                    + cmtBackPath.replace(WyConsts.COMMENTS_DIR, ""));
            BasicFileUtil.renameFile(cmtPath, cmtBackPath);
        }
    }

    /**
     * 恢复备份的评论文件
     *
     * @param newsModule
     * @param code
     */
    public static void recoverCmtFileAfterTask(NewsModule newsModule,
                                               String code) {
        String cmtPath = WyNewsUtil.getCommentLocation(newsModule, code);
        String cmtBackPath = cmtPath + ".bak";
        if (BasicFileUtil.isExistFile(cmtPath)) {
            if (BasicFileUtil.isExistFile(cmtBackPath)) {
                WyNewsUtil.log("CmtTask成功.删除备份文件:"
                        + cmtBackPath.replace(WyConsts.COMMENTS_DIR, ""));
                BasicFileUtil.deleteFile(cmtBackPath);
            }
        } else {
            if (BasicFileUtil.isExistFile(cmtBackPath)) {
                WyNewsUtil.log("CmtTask没有成功生成文件,将备份文件"
                        + cmtBackPath.replace(WyConsts.COMMENTS_DIR, "")
                        + "改回来");
                BasicFileUtil.renameFile(cmtBackPath, cmtPath);
            }
        }
    }

    /**
     * 新闻code是否合法
     *
     * @param code
     * @return
     */
    public static boolean isValidCode(String code) {
        return StringUtils.isNotBlank(code) && code.length() > 10;
    }
}