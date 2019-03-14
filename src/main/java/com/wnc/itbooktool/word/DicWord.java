
package com.wnc.itbooktool.word;

import com.alibaba.fastjson.JSON;

public class DicWord
{
    private Integer id;
    private Integer topic_id;
    private String base_word;
    private String word_third;
    private String word_done;
    private String word_pl;
    private String word_ing;
    private String word_past;
    private String word_er;
    private String word_est;
    private String cn_mean;
    private String book_name;
    private Integer weight;

    public Integer getTopic_id()
    {
        return topic_id;
    }

    public void setTopic_id( Integer topic_id )
    {
        this.topic_id = topic_id;
    }

    public String getBase_word()
    {
        return base_word;
    }

    public void setBase_word( String base_word )
    {
        this.base_word = base_word;
    }

    public String getWord_third()
    {
        return word_third;
    }

    public void setWord_third( String word_third )
    {
        this.word_third = word_third;
    }

    public String getWord_done()
    {
        return word_done;
    }

    public void setWord_done( String word_done )
    {
        this.word_done = word_done;
    }

    public String getWord_pl()
    {
        return word_pl;
    }

    public void setWord_pl( String word_pl )
    {
        this.word_pl = word_pl;
    }

    public String getWord_ing()
    {
        return word_ing;
    }

    public void setWord_ing( String word_ing )
    {
        this.word_ing = word_ing;
    }

    public String getWord_past()
    {
        return word_past;
    }

    public void setWord_past( String word_past )
    {
        this.word_past = word_past;
    }

    public String getWord_er()
    {
        return word_er;
    }

    public void setWord_er( String word_er )
    {
        this.word_er = word_er;
    }

    public String getWord_est()
    {
        return word_est;
    }

    public void setWord_est( String word_est )
    {
        this.word_est = word_est;
    }

    public String getCn_mean()
    {
        return cn_mean;
    }

    public void setCn_mean( String cn_mean )
    {
        this.cn_mean = cn_mean;
    }

    public String getBook_name()
    {
        return book_name;
    }

    public void setBook_name( String book_name )
    {
        this.book_name = book_name;
    }

    @Override
    public String toString()
    {
        return JSON.toJSONString(this);
    }

    public Integer getId()
    {
        return id;
    }

    public void setId( Integer id )
    {
        this.id = id;
    }

    public Integer getWeight()
    {
        return weight;
    }

    public void setWeight( Integer weight )
    {
        this.weight = weight;
    }

}