
package com.wnc.wynews.news;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.wnc.basic.BasicFileUtil;
import com.wnc.string.PatternUtil;
import com.wnc.tools.FileOp;
import com.wnc.wynews.consts.WyConsts;
import com.wnc.wynews.model.News;
import com.wnc.wynews.utils.WyNewsUtil;

/***
 * @Description 生成各模块的新闻ID列表,保存到一个json文件
 * @Date 2018/7/28 10:49
 * @Param null
 * @Return
 */
public class NewsModuleIdsTest
{
    private Map<String, List<String>> map = new ConcurrentHashMap<String, List<String>>();

    @Test
    public void a()
    {
        String folder = WyConsts.NEWS_LIST_DIR;
        String key = null;
        List<String> lines = null;
        for ( File f : new File( folder ).listFiles() )
        {
            key = PatternUtil.getFirstPatternGroup( f.getName(), "(.*?)\\." );
            map.put( key, new ArrayList<String>() );
            lines = FileOp.readFrom( f.getAbsolutePath(), "UTF-8" );
            for ( String line : lines )
            {
                News news = JSONObject.parseObject( line, News.class );
                String code = WyNewsUtil.getNewsCode( news );
                if ( code.length() < 10 )
                {
                    System.out.println( "无法计算评论页code..." + key + " / "
                            + JSONObject.toJSONString( news ) );
                } else
                {
                    List<String> strings = map.get( key );
                    if ( !strings.contains( code ) )
                    {
                        strings.add( code );
                    }
                }
            }
        }

        BasicFileUtil.writeFileString( WyConsts.NEWS_MODULE_IDS,
                JSONObject.toJSONString( map ), null, false );
    }

}
