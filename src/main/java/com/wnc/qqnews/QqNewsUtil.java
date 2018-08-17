
package com.wnc.qqnews;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wnc.basic.BasicDateUtil;
import com.wnc.basic.BasicFileUtil;
import com.wnc.tools.FileOp;
import com.wnc.wynews.model.NewsModule;

public class QqNewsUtil
{

    /**
     * @Description 获取评论保存地址
     * @Date 2018/7/27 18:08
     * @Param module
     * @Param news
     * @Return java.lang.String
     */
    public static String getCommentLocation( NewsModule module, String code )
    {
        String folder = QqConsts.COMMENTS_DIR + module.getName();
        if ( !BasicFileUtil.isExistFolder( folder ) )
        {
            BasicFileUtil.makeDirectory( folder );
        }
        return folder + "\\cmt-" + code + ".txt";
    }

    /****
     * @Description 获取统一格式的时间 数读.txt: [2018-07-22 21:06:25] 新车.txt: [2018-07-26]
     *              旅游.txt: [07/26/2018 01:19:01]
     */
    public static String getFormatTimeStr( String timeStr )
    {
        final String defaultTimeStr = "2018-07-26 12:00:00";
        if ( StringUtils.isNotBlank( timeStr ) )
        {
            if ( timeStr.contains( "/" ) )
            {
                return BasicDateUtil.getLocalDataFromFormatString( timeStr,
                        "MM/dd/yyyy HH:mm:ss" );
            } else if ( timeStr.contains( ":" ) )
            {
                return BasicDateUtil.getLocalDataFromFormatString( timeStr,
                        "yyyy-MM-dd HH:mm:ss" );
            } else if ( timeStr.length() == 10 )
            {
                return BasicDateUtil.getLocalDataFromFormatString( timeStr,
                        "yyyy-MM-dd" );
            }
        }
        return defaultTimeStr;
    }

    private static Set<NewsModule> set = new HashSet<NewsModule>();

    public static Set<NewsModule> getNewsModules()
    {
        if ( set.size() == 0 )
        {
            module();
        }
        return set;
    }

    private static void module()
    {
        List<String> list = FileOp.readFrom( QqConsts.QQNEWS_CONF );
        NewsModule module = JSONObject
                .parseObject( StringUtils.join( list, "" ), NewsModule.class );
        System.out.println( module );
        printLeaf( module );
    }

    private static void printLeaf( NewsModule newsModule )
    {
        if ( CollectionUtils.isEmpty( newsModule.getNodes() ) )
        {
            if ( !newsModule.getIgnore() )
            {
                System.out.println( newsModule.getName() );
                set.add( newsModule );
            }
        } else
        {
            for ( NewsModule newsModule2 : newsModule.getNodes() )
            {
                printLeaf( newsModule2 );
            }
        }
    }

    public static void log( String msg )
    {
        String line = BasicDateUtil.getCurrentDateTimeString() + " " + msg
                + "\r\n";
        BasicFileUtil.writeFileString( QqConsts.LOG_TXT, line, null, true );
    }

    public static void err( NewsModule newsModule, String msg )
    {
        String line = msg;
        if ( newsModule != null )
        {
            line = BasicDateUtil.getCurrentDateTimeString() + " "
                    + newsModule.getName() + "{ " + msg + "}\r\n";
        }
        BasicFileUtil.writeFileString( QqConsts.ERR_TXT, line, null, true );
    }

    /**
     * 抓评论之前备份文件
     * 
     * @param newsModule
     * @param code
     */
    public static void backupCmtFileBeforeTask( NewsModule newsModule,
            String code )
    {
        // 处理评论文件, 原有的做备份
        String cmtPath = getCommentLocation( newsModule, code );
        String cmtBackPath = cmtPath + ".bak";
        if ( BasicFileUtil.isExistFile( cmtBackPath ) )
        {
            log( "QqNewsModuleTask删除备份文件:"
                    + cmtBackPath.replace( QqConsts.COMMENTS_DIR, "" ) );
            BasicFileUtil.deleteFile( cmtBackPath );
        }
        if ( BasicFileUtil.isExistFile( cmtPath ) )
        {
            log( "QqNewsModuleTask创建备份文件:"
                    + cmtBackPath.replace( QqConsts.COMMENTS_DIR, "" ) );
            BasicFileUtil.renameFile( cmtPath, cmtBackPath );
        }
    }

    /**
     * 恢复备份的评论文件
     * 
     * @param newsModule
     * @param code
     */
    public static void recoverCmtFileAfterTask( NewsModule newsModule,
            String code )
    {
        String cmtPath = getCommentLocation( newsModule, code );
        String cmtBackPath = cmtPath + ".bak";
        if ( BasicFileUtil.isExistFile( cmtPath ) )
        {
            if ( BasicFileUtil.isExistFile( cmtBackPath ) )
            {
                log( "CmtTask成功.删除备份文件:"
                        + cmtBackPath.replace( QqConsts.COMMENTS_DIR, "" ) );
                BasicFileUtil.deleteFile( cmtBackPath );
            }
        } else
        {
            if ( BasicFileUtil.isExistFile( cmtBackPath ) )
            {
                log( "CmtTask没有成功生成文件,将备份文件"
                        + cmtBackPath.replace( QqConsts.COMMENTS_DIR, "" )
                        + "改回来" );
                BasicFileUtil.renameFile( cmtBackPath, cmtPath );
            }
        }
    }

    /**
     * 评论code是否合法
     * 
     * @param code
     * @return
     */
    public static boolean isValidCode( String code )
    {
        return StringUtils.isNotBlank( code );
    }

    public static JSONArray getNewsList( String html )
    {
        return JSONObject.parseObject( html ).getJSONArray( "data" );
    }

    public static void errUser( String msg )
    {
        BasicFileUtil.writeFileString( QqConsts.USERS_DIR + "user-err.txt",
                BasicDateUtil.getCurrentDateTimeString() + " " + msg + "\r\n",
                null, true );
    }
}