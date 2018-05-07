
package com.wnc.sboot1.itbook.entity;

public class WordExample
{
    private String word;
    private String q;
    private int qid;
    private String excerpt;
    private String href;
    private int votes;
    private int answers;
    /**
     * 是否收藏
     */
    private int star;

    public String getWord()
    {
        return word;
    }

    public void setWord( String word )
    {
        this.word = word;
    }

    public String getQ()
    {
        return q;
    }

    public void setQ( String q )
    {
        this.q = q;
    }

    public String getHref()
    {
        return href;
    }

    public void setHref( String href )
    {
        this.href = href;
    }

    public String getExcerpt()
    {
        return excerpt;
    }

    public void setExcerpt( String excerpt )
    {
        this.excerpt = excerpt;
    }

    public int getVotes()
    {
        return votes;
    }

    public void setVotes( int votes )
    {
        this.votes = votes;
    }

    public int getAnswers()
    {
        return answers;
    }

    public void setAnswers( int answers )
    {
        this.answers = answers;
    }

    public int getQid()
    {
        return qid;
    }

    public void setQid( int qid )
    {
        this.qid = qid;
    }

    public int getStar()
    {
        return star;
    }

    public void setStar( int star )
    {
        this.star = star;
    }

}