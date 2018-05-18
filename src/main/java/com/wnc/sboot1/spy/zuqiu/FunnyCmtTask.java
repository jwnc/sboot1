
package com.wnc.sboot1.spy.zuqiu;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawl.spider.SpiderHttpClient;
import com.crawl.spider.entity.Page;
import com.crawl.spider.task.AbstractPageTask;
import com.wnc.sboot1.spy.util.SpiderUtils;

public class FunnyCmtTask extends AbstractPageTask
{
    private String pinglun;
    private static final int HOT_AT_LEAST = 4;
    private static final int FUNNY_RATE = 20; // 判断是否有趣的依据
    final Zb8News news;

    private FunnyCommetSpy funnyCommetSpy;

    public FunnyCmtTask( FunnyCommetSpy funnyCommetSpy,Zb8News news )
    {
        this.MAX_RETRY_TIMES = 20;
        this.funnyCommetSpy = funnyCommetSpy;
        this.news = news;
        this.pinglun = news.getPinglun();
        this.url = getHotCountUrl( pinglun );

        this.proxyFlag = true;
        this.MAX_RETRY_TIMES = 8;
    }

    public Zb8News getZb8News()
    {
        return this.news;
    }

    @Override
    protected void retry()
    {
        if ( funnyCommetSpy.isTaskOver() )
        {
            return;
        }
        funnyCommetSpy.getPageThreadExecutor()
                .execute( new FunnyCmtTask( funnyCommetSpy, news ) );
    }

    @Override
    protected void handle( Page page ) throws Exception
    {
        // 如果已经结束了, 当前正在执行的任务也不能往Map里面添加了, 防止concurrent错误
        if ( funnyCommetSpy.isTaskOver() )
        {
            return;
        }
        int hot = JSONObject.parseObject( page.getHtml() )
                .getIntValue( "hot_num" );
        if ( hot >= HOT_AT_LEAST )
        {
            List<HotComment> parseCommentList = parseCommentList( pinglun );
            if ( parseCommentList.size() > 0 )
            {
                funnyCommetSpy.save( news, parseCommentList );
            }
        }
    }

    public void complete( int type, String msg )
    {
        super.complete( type, msg );
        msg = Thread.currentThread().getName() + " " + removeRandom()
                + (StringUtils.isNotBlank( msg ) ? (" Msg:" + msg) : "");
        funnyCommetSpy.callBackComplete( type, msg, this );
        SpiderHttpClient.parseCount.getAndIncrement();
    }

    @Override
    protected String removeRandom()
    {
        return url.replaceAll( "\\?key=\\S+", "" );
    }

    private List<HotComment> parseCommentList( String pinglun ) throws Exception
    {
        List<HotComment> list = new ArrayList<HotComment>();
        String jsonHtml = SpiderUtils.getHtmlByProxy( getHotListUrl( pinglun ),
                currentProxy );
        // 针对返回404找不到评论的新闻
        if ( ("404 Not Found").equals( Jsoup.parse( jsonHtml ).title() ) )
        {
            return list;
        }

        List<HotComment> parseArray = JSONArray.parseArray( jsonHtml,
                HotComment.class );
        for ( HotComment hotComment : parseArray )
        {
            if ( isFunny( hotComment ) )
            {
                list.add( hotComment );
            }
        }
        return list;
    }

    private boolean isFunny( HotComment hotComment )
    {
        double d = hotComment.getDown();
        if ( hotComment.getDown() == 0 )
        {
            d = 0.8;
        }
        return hotComment.getUp() / d >= FUNNY_RATE;
    }

    private String getHotListUrl( String pinglun )
    {
        return String.format( Zb8Const.COMMENT_FORMAT,
                pinglun.replace( "-", "/" ), "hot" ) + Math.random();
    }

    private static String getHotCountUrl( String pinglun )
    {
        return String.format( Zb8Const.COMMENT_FORMAT,
                pinglun.replace( "-", "/" ), "count" ) + Math.random();
    }

}
