
package com.wnc.sboot1.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.crawl.spider.entity.Page;
import com.wnc.sboot1.cluster.util.PageUtil;
import com.wnc.string.PatternUtil;

@RunWith( SpringRunner.class )
@SpringBootTest
public class ProxyUtilTest
{
    @Test
    public void a() throws IOException
    {
        List<String> get61Proxies = get61Proxies();
        System.out.println( get61Proxies );
    }

    public List<String> get61Proxies() throws IOException
    {

        HttpGet httpGet = new HttpGet(
                "http://118.126.116.16:8080/sboot1/proxy/get66Proxy" );
        Page webPage = PageUtil.getWebPage( httpGet, "UTF-8" );
        String content = webPage.getHtml();
        List<String> get61Proxies = PatternUtil.getAllPatternGroup( content,
                "\\d+.\\d+.\\d+.\\d+:\\d+" );
        List<String> retList = new ArrayList<String>();
        for ( String string : get61Proxies )
        {
            retList.add( string );
        }
        return retList;

    }
}
