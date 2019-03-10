
package com.wnc.wynews.news;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.wnc.basic.BasicFileUtil;
import com.wnc.tools.FileOp;
import com.wnc.wynews.consts.WyConsts;
import com.wnc.wynews.model.News;
import com.wnc.wynews.utils.WyNewsUtil;

/***
 * @Description 新闻按日期分类
 * @Date 2018/7/27 17:12
 * @Param null
 * @Return
 */
public class NewsClassic
{

    /***
     * 数读.txt: [2018-07-22 21:06:25] 新车.txt: [2018-07-26] 旅游.txt: [07/26/2018
     * 01:19:01]
     */
    @Test
    public void a()
    {
        String folder = WyConsts.NEWS_LIST_DIR;
        for ( File f : new File( folder ).listFiles() )
        {
            List<String> lines = FileOp.readFrom( f.getAbsolutePath(),
                    "UTF-8" );
            Set<String> set = new HashSet<String>( lines );
            for ( String line : set )
            {
                News news = JSONObject.parseObject( line, News.class );
                String dateStr = WyNewsUtil.getFormatTimeStr( news.getTime() );
                String day = dateStr.substring( 0, 10 );
                String code = WyNewsUtil.getNewsCode( news );
                System.out.println(
                        f.getName() + " " + code + " : [" + dateStr + "]" );
                BasicFileUtil
                        .writeFileString( WyConsts.NEWS_DAY_DIR + day + ".txt",
                                code + "["
                                        + f.getName().replaceAll( "\\..+", "" )
                                        + "]" + dateStr + "\r\n",
                                null, true );
            }
        }
    }
}
