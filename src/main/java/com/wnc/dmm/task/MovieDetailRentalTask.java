
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
public class MovieDetailRentalTask extends MovieDetailMonoTask
{
    static
    {
        retryMap.put( MovieDetailRentalTask.class,
                new ConcurrentHashMap<String, Integer>() );
    }

    public MovieDetailRentalTask( String cid,String url )
    {
        super( cid, url );
    }

    @Override
    protected String getPublishDate( Element element )
    {
        return getMonoTExt( element, "貸出開始日：", "[/\\d]+" );
    }

    @Override
    protected String getTitle( Document documentResult )
    {
        return getText( documentResult,
                "#mu > div > div.area-headline.group > div > h1" );
    }
}
