
package com.wnc.itbooktool.word;

import com.alibaba.fastjson.JSONArray;
import com.wnc.basic.BasicStringUtil;

public class WordExpand
{
    int topic_id;
    String same;
    String antonym;
    String same_analysis;

    public int getTopic_id()
    {
        return topic_id;
    }

    public void setTopic_id( int topic_id )
    {
        this.topic_id = topic_id;
    }

    public String getSame()
    {
        return same;
    }

    public void setSame( String same )
    {
        this.same = same;
    }

    public String getAntonym()
    {
        return antonym;
    }

    public void setAntonym( String antonym )
    {
        this.antonym = antonym;
    }

    public String getSame_analysis()
    {
        return same_analysis;
    }

    public void setSame_analysis( String same_analysis )
    {
        this.same_analysis = same_analysis;
    }

    @Override
    public String toString()
    {
        String ret = "";
        ret += getStringFromJson( same, "近义词" );
        ret += getStringFromJson( antonym, "反义词" );
        ret += getStringFromJson( same_analysis, "同义词分析" );
        return ret;
    }

    private String getStringFromJson( String json, String tip )
    {
        if ( BasicStringUtil.isNullString( json ) )
        {
            return "";
        }
        String strFromJson = tip + "\n";
        JSONArray arr = JSONArray.parseArray( json );
        for ( int i = 0; i < arr.size(); i++ )
        {
            // 每一种词性
            String part = "";
            String part_name = arr.getJSONObject( i ).getString( "part_name" );
            part += part_name + "\n";
            if ( !tip.contains( "分析" ) )
            {
                JSONArray means = arr.getJSONObject( i )
                        .getJSONArray( "means" );
                for ( int j = 0; j < means.size(); j++ )
                {
                    // 每一种意思
                    part += means.getJSONObject( j ).getString( "word_mean" )
                            + "\n";
                    part += means.getJSONObject( j ).getString( "cis" ) + "\n";
                    if ( j < means.size() )
                    {
                        part += "\n";
                    }
                }
            } else
            {
                part += arr.getJSONObject( i ).getString( "word_list" ) + "\n";
                part += arr.getJSONObject( i ).getString( "means" ) + "\n";
            }
            strFromJson += part;
        }

        return strFromJson + "\n";
    }
}