
package com.wnc.wynews.news;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.wnc.basic.BasicFileUtil;
import com.wnc.tools.FileOp;
import com.wnc.wynews.consts.WyConsts;
import com.wnc.wynews.model.NewsModule;
import com.wnc.wynews.utils.WyNewsUtil;

/**
 * @Description 评论按模块分类
 * @Author nengcai.wang
 * @Date 2018/7/28 10:41
 */
public class CommentClassic
{

    @Test
    public void x()
    {
        Set<NewsModule> list = WyNewsUtil.getNewsModules();
        for ( NewsModule m : list )
        {
            BasicFileUtil.makeDirectory(
                    WyConsts.COMMENTS_DIR + m.getName() + "\\" );
        }
    }

    private Map<String, List<String>> map = new ConcurrentHashMap<String, List<String>>();
    private String moduleIdsFile = WyConsts.NEWS_MODULE_IDS;

    @Test
    public void a()
    {
        if ( BasicFileUtil.isExistFile( moduleIdsFile ) )
        {
            List<String> lines = FileOp.readFrom( moduleIdsFile, "UTF-8" );
            String join = StringUtils.join( lines, "" );
            map = JSONObject.parseObject( join, ConcurrentHashMap.class );
            for ( Map.Entry<String, List<String>> entry : map.entrySet() )
            {
                for ( String code : entry.getValue() )
                {
                    // System.out.println(code);
                    String path = WyConsts.COMMENTS_DIR + "2018-07-26\\cmt-"
                            + code + ".txt";
                    System.out.println( path );
                    if ( BasicFileUtil.isExistFile( path ) )
                    {
                        BasicFileUtil.renameFile( path, WyConsts.COMMENTS_DIR
                                + entry.getKey() + "\\cmt-" + code + ".txt" );
                    }
                }
            }
        }
    }
}
