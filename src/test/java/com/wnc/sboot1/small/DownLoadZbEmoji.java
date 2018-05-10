
package com.wnc.sboot1.small;

import java.io.File;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawl.core.util.HttpClientUtil;
import com.wnc.tools.UrlPicDownloader;

public class DownLoadZbEmoji
{
    public static void main( String[] args ) throws Exception
    {
        String url = "https://m.zhibo8.cc/json/emoji/index.php";
        String webPage = HttpClientUtil.getWebPage( url );
        JSONArray parseArray = JSONArray.parseArray( webPage );
        for ( int i = 0; i < parseArray.size(); i++ )
        {
            JSONObject jsonObject = parseArray.getJSONObject( i );
            JSONArray jsonArray = jsonObject.getJSONArray( "list" );
            for ( int j = 0; j < jsonArray.size(); j++ )
            {
                JSONObject jsonObject2 = jsonArray.getJSONObject( j );
                String name_cn = jsonObject2.getString( "name_cn" );
                String src = jsonObject2.getString( "src" );
                System.out.println( "['" + name_cn + "','" + src + "']," );
                String replace = src.replace( "@3x", "@2x" );
                UrlPicDownloader.download( replace,
                        "D:\\projects\\diy\\sboot1-master\\src\\main\\resources\\static\\images\\"
                                + new File( replace ).getName() );
            }
        }
    }
}
