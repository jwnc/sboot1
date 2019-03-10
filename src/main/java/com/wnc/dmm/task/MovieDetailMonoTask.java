
package com.wnc.dmm.task;

import java.util.concurrent.ConcurrentHashMap;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * √-http://www.dmm.co.jp/digital/videoc/-/detail/=/cid=realf137/
 * ×-http://www.dmm.co.jp/digital/videoa/-/detail/=/cid=realf137/
 * √-http://www.dmm.co.jp/mono/dvd/-/detail/=/cid=h_237nacr147/
 * ×-http://www.dmm.co.jp/digital/videoa/-/detail/=/cid=h_237nacr147/
 * 
 * @author nengcai.wang
 */
public class MovieDetailMonoTask extends MovieDetailTask
{
    static
    {
        retryMap.put( MovieDetailMonoTask.class,
                new ConcurrentHashMap<String, Integer>() );
    }

    public MovieDetailMonoTask( String cid )
    {
        super( cid, 1 );
    }

    public MovieDetailMonoTask( String cid,String url )
    {
        super( cid, url );
    }

    @Override
    protected String getPublishDate( Element element )
    {
        return getMonoTExt( element, "発売日：", "[/\\d]+" );
    }

    @Override
    protected String getDesc( Document documentResult )
    {
        Element first = documentResult.select(
                "#mu > div > table > tbody > tr > td:nth-child(1) > div.mg-b20.lh4 > p" )
                .first();
        if ( first != null )
        {
            return first.ownText();
        }
        return "";
    }

}
