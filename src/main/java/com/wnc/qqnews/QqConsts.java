
package com.wnc.qqnews;

import com.wnc.basic.BasicFileUtil;

public class QqConsts
{
    // 设置项目路径
    public static String APP_DIR = "C:\\data\\spider\\qq-news\\";
    public final static String NEWS_MENU_DIR = APP_DIR + "news-menu\\";
    public final static String NEWS_LIST_DIR = APP_DIR + "news-list\\";
    public final static String NEWS_DAY_DIR = APP_DIR + "news-day\\";
    public final static String COMMENTS_DIR = APP_DIR + "comments\\";
    public final static String USERS_DIR = APP_DIR + "users\\";
    public final static String USERS_TXT = USERS_DIR + "users.txt";
    public final static String USERMETA_TXT = USERS_DIR + "usermeta.txt";

    public final static String MODULE_HISTORY = APP_DIR + "module-history\\";
    public final static String NEWS_MODULE_IDS = APP_DIR
            + "qqnews-module-ids.txt";

    public final static String QQNEWS_CONF = APP_DIR + "qqnews-conf.txt";
    public final static String LOG_TXT = APP_DIR + "log.txt";
    public final static String ERR_TXT = APP_DIR + "err.txt";
    public final static String ARTICLES_TXT = APP_DIR + "articles.txt";

    static
    {
        BasicFileUtil.makeDirectory( APP_DIR );
        BasicFileUtil.makeDirectory( NEWS_MENU_DIR );
        BasicFileUtil.makeDirectory( NEWS_LIST_DIR );
        BasicFileUtil.makeDirectory( NEWS_DAY_DIR );
        BasicFileUtil.makeDirectory( COMMENTS_DIR );
        BasicFileUtil.makeDirectory( USERS_DIR );
        BasicFileUtil.makeDirectory( MODULE_HISTORY );
    }
}
