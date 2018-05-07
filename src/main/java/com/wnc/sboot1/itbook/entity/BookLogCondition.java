
package com.wnc.sboot1.itbook.entity;

public class BookLogCondition
{
    private String word;
    private String dayStart;
    private String dayEnd;
    private String device;
    private String type;
    private Integer weightStart;
    private Integer weightEnd;
    private Integer star;

    public String getWord()
    {
        return word;
    }

    public void setWord( String word )
    {
        this.word = word;
    }

    public String getDayStart()
    {
        return dayStart;
    }

    public void setDayStart( String dayStart )
    {
        this.dayStart = dayStart;
    }

    public String getDayEnd()
    {
        return dayEnd;
    }

    public void setDayEnd( String dayEnd )
    {
        this.dayEnd = dayEnd;
    }

    public String getDevice()
    {
        return device;
    }

    public void setDevice( String device )
    {
        this.device = device;
    }

    public String getType()
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }

    public Integer getWeightStart()
    {
        return weightStart;
    }

    public void setWeightStart( Integer weightStart )
    {
        this.weightStart = weightStart;
    }

    public Integer getWeightEnd()
    {
        return weightEnd;
    }

    public void setWeightEnd( Integer weightEnd )
    {
        this.weightEnd = weightEnd;
    }

    public Integer getStar()
    {
        return star;
    }

    public void setStar( Integer star )
    {
        this.star = star;
    }
}