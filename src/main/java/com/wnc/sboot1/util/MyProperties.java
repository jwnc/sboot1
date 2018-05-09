
package com.wnc.sboot1.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MyProperties
{
    @Value( "${com.wnc.title}" )
    private String title;
    @Value( "${com.wnc.description}" )
    private String description;
    @Value( "${itbook.root}" )
    private String itbookRoot;

    public String getTitle()
    {
        return title;
    }

    public void setTitle( String title )
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public String getItbookRoot()
    {
        return itbookRoot;
    }

    public void setItbookRoot( String itbookRoot )
    {
        this.itbookRoot = itbookRoot;
    }

}