package com.wnc.itbooktool.word;

public class Topic
{
    private String topic_base_word;
    private String bookName;
    private Integer topic_id;
    private String matched_word;
    private String mean_cn;
    private String state;

    public Topic()
    {

    }

    public String getBookName()
    {
        return bookName;
    }

    public void setBookName( String bookName )
    {
        this.bookName = bookName;
    }

    public Integer getTopic_id()
    {
        return topic_id;
    }

    public void setTopic_id( Integer topic_id )
    {
        this.topic_id = topic_id;
    }

    public String getMatched_word()
    {
        return matched_word;
    }

    public void setMatched_word( String word )
    {
        this.matched_word = word;
    }

    public String getMean_cn()
    {
        return mean_cn;
    }

    public void setMean_cn( String mean_cn )
    {
        this.mean_cn = mean_cn;
    }

    @Override
    public String toString()
    {
        return "Topic [topic_base_word=" + topic_base_word + ", bookName="
                + bookName + ", topic_id=" + topic_id + ", matched_word="
                + matched_word + ", mean_cn=" + mean_cn + ", state=" + state
                + "]";
    }

    public String getTopic_base_word()
    {
        return topic_base_word;
    }

    public void setTopic_base_word( String topic_base_word )
    {
        this.topic_base_word = topic_base_word;
    }

    public String getState()
    {
        return state;
    }

    public void setState( String state )
    {
        this.state = state;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((matched_word == null) ? 0 : matched_word.toLowerCase()
                        .hashCode());
        return result;
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj )
        {
            return true;
        }
        if ( obj == null )
        {
            return false;
        }
        if ( getClass() != obj.getClass() )
        {
            return false;
        }
        Topic other = (Topic)obj;
        if ( !matched_word.equalsIgnoreCase( other.matched_word ) )
        {
            return false;
        }

        return true;
    }
}