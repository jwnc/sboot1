
package com.wnc.wynews.news;

import java.io.File;
import java.util.List;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.wnc.basic.BasicFileUtil;
import com.wnc.tools.FileOp;
import com.wnc.wynews.consts.WyConsts;
import com.wnc.wynews.model.News;
import com.wnc.wynews.utils.WyNewsUtil;

/***
 * @Description 获取每个模块的最后时间, 并重写日志文件
 * @Date 2018/7/27 17:12
 * @Param null
 * @Return
 */
public class SeekModuleHistory
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
            News news = JSONObject.parseObject( lines.get( 0 ), News.class );
            System.out.println( f.getName() + ": ["
                    + WyNewsUtil.getFormatTimeStr( news.getTime() ) + "]" );
            BasicFileUtil.writeFileString(
                    WyConsts.MODULE_HISTORY + f.getName(), news.getTime(), null,
                    false );
        }
    }

    @Test
    public void date0728()
    {
        String folder = WyConsts.NEWS_LIST_DIR;
        for ( File f : new File( folder ).listFiles() )
        {
            BasicFileUtil.writeFileString(
                    WyConsts.MODULE_HISTORY + f.getName(),
                    "2018-07-28 12:00:00", null, false );
        }
    }
}
