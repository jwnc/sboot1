
package com.wnc.sboot1.spy.zhihu;

import com.crawl.proxy.entity.Proxy;
import com.crawl.spider.entity.Page;
import com.crawl.spider.task.AbstractPageTask;

/**
 * GeneralPageTask 下载初始化authorization字段页面
 */
public class GeneralPageTask extends AbstractPageTask
{
    private Page page = null;

    public GeneralPageTask( String url,boolean proxyFlag )
    {
        super( url, proxyFlag );
    }

    @Override
    protected void retry()
    {
        try
        {
            Thread.sleep( 100 );
        } catch ( InterruptedException e )
        {
            e.printStackTrace();
        }
        this.run();// 继续下载
    }

    @Override
    protected void handle( Page page )
    {
        System.out.println(
                "Page:" + page.getHtml() + " " + currentProxy.getProxyStr() );
        this.page = page;
    }

    public Page getPage()
    {
        return this.page;
    }

    public Proxy getWrongProxy()
    {
        return this.currentProxy;
    }

}
