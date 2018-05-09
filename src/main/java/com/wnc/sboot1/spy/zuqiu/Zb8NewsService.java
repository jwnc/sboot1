
package com.wnc.sboot1.spy.zuqiu;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wnc.sboot1.spy.util.SpiderUtils;

public class Zb8NewsService
{
    public List<Zb8News> getNews( String catelog, String day ) throws Exception
    {
        List<Zb8News> list = new ArrayList<>();
        String url = String.format( Zb8Const.NEWS_FORMAT, catelog, day );
        String html = SpiderUtils.getJsonHtml( url );

        JSONArray parseArray = JSONObject.parseObject( html )
                .getJSONArray( "video_arr" );
        Zb8News news;
        for ( int i = 0; i < parseArray.size(); i++ )
        {
            news = parseArray.getJSONObject( i ).toJavaObject( Zb8News.class );
            list.add( news );
        }
        return list;
    }
}
