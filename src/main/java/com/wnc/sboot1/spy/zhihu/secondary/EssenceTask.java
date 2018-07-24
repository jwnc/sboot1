
package com.wnc.sboot1.spy.zhihu.secondary;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.client.methods.HttpGet;

import com.alibaba.fastjson.JSONObject;
import com.crawl.core.util.HttpClientUtil;
import com.crawl.spider.task.AbstractPageTask;
import com.wnc.basic.BasicFileUtil;
import com.wnc.sboot1.spy.zhihu.GeneralPageTask;

public class EssenceTask
{
    public static void main( String[] args ) throws IOException
    {
        try
        {
            seek();
        } catch ( Exception e )
        {
            e.printStackTrace();
            System.out.println( skip + "处失败" );
        }
    }

    static int skip = 960;
    static final int pageSize = 10;
    static final String urlBase = "https://www.zhihu.com/api/v4/topics/19639176/feeds/essence?include=data%5B%3F%28target.type%3Dtopic_sticky_module%29%5D.target.data%5B%3F%28target.type%3Danswer%29%5D.target.content%2Crelationship.is_authorized%2Cis_author%2Cvoting%2Cis_thanked%2Cis_nothelp%3Bdata%5B%3F%28target.type%3Dtopic_sticky_module%29%5D.target.data%5B%3F%28target.type%3Danswer%29%5D.target.is_normal%2Ccomment_count%2Cvoteup_count%2Ccontent%2Crelevant_info%2Cexcerpt.author.badge%5B%3F%28type%3Dbest_answerer%29%5D.topics%3Bdata%5B%3F%28target.type%3Dtopic_sticky_module%29%5D.target.data%5B%3F%28target.type%3Darticle%29%5D.target.content%2Cvoteup_count%2Ccomment_count%2Cvoting%2Cauthor.badge%5B%3F%28type%3Dbest_answerer%29%5D.topics%3Bdata%5B%3F%28target.type%3Dtopic_sticky_module%29%5D.target.data%5B%3F%28target.type%3Dpeople%29%5D.target.answer_count%2Carticles_count%2Cgender%2Cfollower_count%2Cis_followed%2Cis_following%2Cbadge%5B%3F%28type%3Dbest_answerer%29%5D.topics%3Bdata%5B%3F%28target.type%3Danswer%29%5D.target.content%2Crelationship.is_authorized%2Cis_author%2Cvoting%2Cis_thanked%2Cis_nothelp%3Bdata%5B%3F%28target.type%3Danswer%29%5D.target.author.badge%5B%3F%28type%3Dbest_answerer%29%5D.topics%3Bdata%5B%3F%28target.type%3Darticle%29%5D.target.content%2Cauthor.badge%5B%3F%28type%3Dbest_answerer%29%5D.topics%3Bdata%5B%3F%28target.type%3Dquestion%29%5D.target.comment_count&limit=10&offset=";

    private static void seek() throws IOException, InterruptedException
    {
        AbstractPageTask.retryMap.put( GeneralPageTask.class,
                new HashMap<String, Integer>() );
        while ( true )
        {
            HttpGet request = new HttpGet( urlBase + skip );
            // request.setHeader( "authorization",
            // "oauth " + TT2.initAuthorization() );
            String webPage = HttpClientUtil.getWebPage( request );
            BasicFileUtil.writeFileString(
                    "D:\\个人工作\\spy\\zhihu\\topics\\表情图\\表情图-" + skip + ".json",
                    webPage, null, false );
            boolean is_end = JSONObject.parseObject( webPage )
                    .getJSONObject( "paging" ).getBooleanValue( "is_end" );
            if ( is_end )
            {
                System.out.println( "成功结束!" );
                break;
            }
            skip += pageSize;
            Thread.sleep( 2000 );
        }
    }
}
