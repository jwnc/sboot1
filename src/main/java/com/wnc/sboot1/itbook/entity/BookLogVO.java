
package com.wnc.sboot1.itbook.entity;

import com.wnc.itbooktool.BookLog;

public class BookLogVO extends BookLog
{
    private String style;
    private String mean;
    private String device;
    private String deviceCnName;
    private int weight;
    private int deleted = 0;
    private int id;

    public String getStyle()
    {
        return style;
    }

    public void setStyle( String style )
    {
        this.style = style;
    }

    public String getMean()
    {
        return mean;
    }

    public void setMean( String mean )
    {
        this.mean = mean;
    }

    public String getDevice()
    {
        return device;
    }

    public void setDevice( String device )
    {
        this.device = device;
    }

    public int getWeight()
    {
        return weight;
    }

    public void setWeight( int weight )
    {
        this.weight = weight;
    }

    @Override
    public String toString()
    {
        return "BookLogVO [style=" + style + ", mean=" + mean + ", device="
                + device + ", weight=" + weight + ", toString()="
                + super.toString() + "]";
    }

    public int getDeleted()
    {
        return deleted;
    }

    public void setDeleted( int deleted )
    {
        this.deleted = deleted;
    }

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public String getDeviceCnName()
    {
        return deviceCnName;
    }

    public void setDeviceCnName( String deviceCnName )
    {
        this.deviceCnName = deviceCnName;
    }

}