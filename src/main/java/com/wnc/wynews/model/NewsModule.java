
package com.wnc.wynews.model;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wnc.basic.BasicDateUtil;

public class NewsModule
{
    private String url;
    private String name;
    private List<NewsModule> nodes;
    private String more;
    private Boolean ignore = false;
    private Date lastSpyDate;

    public NewsModule()
    {

    }

    public NewsModule( String name,Date date )
    {
        this.name = name;
        this.lastSpyDate = date;
    }

    public NewsModule( String name,String date )
    {
        this.name = name;
        this.lastSpyDate = BasicDateUtil.getLocaleString2Date( date );
    }

    public boolean isOnePageModule()
    {
        return StringUtils.isBlank( more )
                || (url != null && url.equals( more ));
    }

    /***
     * @Description 判断是否为ajax加载页面
     * @Date 2018/7/26 12:30
     * @Param
     * @Return boolean
     */
    public boolean isAjaxLoadMore()
    {
        return StringUtils.endsWith( more, ".js" )
                || StringUtils.contains( more, "datalist" );
    }

    public Date getLastSpyDate()
    {
        return lastSpyDate;
    }

    public void setLastSpyDate( Date lastSpyDate )
    {
        this.lastSpyDate = lastSpyDate;
    }

    public void setLastSpyDate( String lastSpyDate )
    {
        this.lastSpyDate = BasicDateUtil.getLocaleString2Date( lastSpyDate );
    }

    public Boolean getIgnore()
    {
        return ignore;
    }

    public void setIgnore( Boolean ignore )
    {
        this.ignore = ignore;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl( String url )
    {
        this.url = url;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public List<NewsModule> getNodes()
    {
        return nodes;
    }

    public void setNodes( List<NewsModule> nodes )
    {
        this.nodes = nodes;
    }

    public String getMore()
    {
        return more;
    }

    public void setMore( String more )
    {
        this.more = more;
    }

}
