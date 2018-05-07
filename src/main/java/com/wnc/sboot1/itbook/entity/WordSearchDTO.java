package com.wnc.sboot1.itbook.entity;

public class WordSearchDTO
{
    private String word;
    private int dictId;
    private String orginalWord;
    private int page = 1;
    private String searchUrl;
    private int resultCount;
    private int displayListSize;

    public String getWord()
    {
        return word;
    }

    public void setWord( String word )
    {
        this.word = word;
    }

    public String getSearchUrl()
    {
        return searchUrl;
    }

    public void setSearchUrl( String searchUrl )
    {
        this.searchUrl = searchUrl;
    }

    public int getResultCount()
    {
        return resultCount;
    }

    public void setResultCount( int resultCount )
    {
        this.resultCount = resultCount;
    }

    public int getDisplayListSize()
    {
        return displayListSize;
    }

    public void setDisplayListSize( int displayListSize )
    {
        this.displayListSize = displayListSize;
    }

    public String getOrginalWord()
    {
        return orginalWord;
    }

    public void setOrginalWord( String orginalWord )
    {
        this.orginalWord = orginalWord;
    }

    public int getPage()
    {
        return page;
    }

    public void setPage( int page )
    {
        this.page = page;
    }

    public int getDictId()
    {
        return dictId;
    }

    public void setDictId( int dictId )
    {
        this.dictId = dictId;
    }
}