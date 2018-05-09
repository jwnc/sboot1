
package com.wnc.sboot1.itbook.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONObject;
import com.wnc.basic.BasicNumberUtil;
import com.wnc.sboot1.common.exceptions.CheckException;
import com.wnc.sboot1.itbook.entity.WordExample;
import com.wnc.sboot1.itbook.entity.WordSearchDTO;
import com.wnc.sboot1.itbook.service.WordSearchService;
import com.wnc.string.PatternUtil;

public class StackOverflowUtils
{
    WordSearchDTO wordSearchDTO;
    String word;
    String orginalWord;
    int page;
    int resultCount;
    String searchUrl;
    Document document;

    public StackOverflowUtils( WordSearchDTO wordSearchDTO )
    {
        this.wordSearchDTO = wordSearchDTO;
        this.word = wordSearchDTO.getWord().trim();
        this.orginalWord = wordSearchDTO.getOrginalWord().trim();
        this.page = wordSearchDTO.getPage() < 1 ? 1 : wordSearchDTO.getPage();
        searchUrl = "https://stackoverflow.com/search?page=" + page
                + "&tab=votes&q=" + word
                + " is:question [java] or [sql] or [js] or [css] or [html]";
        initDoc();
    }

    public String getSearchUrl()
    {
        return searchUrl;
    }

    /**
     * 获取搜索结果总数
     * 
     * @return
     * @throws IOException
     */
    public int getResultCount()
    {
        initResultCount( document );
        return resultCount;
    }

    private void initDoc()
    {
        try
        {
            if ( document == null )
            {
                document = Jsoup.connect( searchUrl ).timeout( 5000 )
                        // .proxy( "openproxy.huawei.com", 8080 )
                        .get();
            }
        } catch ( IOException e )
        {
            throw new CheckException( e );
        }
    }

    public static void main( String[] args )
    {
        String string = "<h1 a='1>'>Demo page</h1>";
        String escapeHtml = StringEscapeUtils.escapeHtml( string );
        System.out.println( escapeHtml );
        System.out.println( string.replaceAll( "<.*?>", "" ) );
    }

    public static void main2( String[] args ) throws IOException
    {
        String word = "explict";
        WordSearchDTO wordSearchDTO = new WordSearchDTO();
        wordSearchDTO.setWord( word );
        wordSearchDTO.setOrginalWord( word );
        wordSearchDTO.setPage( 1 );
        StackOverflowUtils stackOverflowUtils = new StackOverflowUtils(
                wordSearchDTO );
        List<WordExample> wordExamples = stackOverflowUtils.getWordExamples();
        System.out.println( JSONObject.toJSON( wordExamples ) );
        System.out.println( wordExamples.size() );
        System.out.println( stackOverflowUtils.getResultCount() );
    }

    public List<WordExample> getWordExamples()
    {
        List<WordExample> examples = new ArrayList<WordExample>();
        try
        {
            WordExample wordExample = null;
            Elements select = document.select( ".search-results .summary" );
            for ( Element element : select )
            {
                boolean fromDbflag = false;
                Element first = element.select( ".result-link a" ).first();
                int qid = BasicNumberUtil
                        .getNumber( PatternUtil.getFirstPatternGroup(
                                first.absUrl( "href" ), "questions/(\\d+)" ) );
                if ( WordSearchService.existQuestion( qid ) )
                {
                    wordExample = WordSearchService.getWordQuestion( qid );
                    fromDbflag = true;
                } else
                {
                    wordExample = new WordExample();
                    wordExample.setExcerpt( element.select( ".excerpt" ).text()
                            .replace( "class=\"result-highlight\"", "" ) );
                    wordExample.setQ( first.text()
                            .replace( "class=\"result-highlight\"", "" ) );
                }
                wordExample.setWord( word );
                wordExample.setHref( first.absUrl( "href" ) );
                wordExample.setQid( qid );

                String text = element.previousElementSibling().text()
                        .replaceAll( "\\s", "" );
                wordExample.setVotes( BasicNumberUtil.getNumber( PatternUtil
                        .getFirstPatternGroup( text, "(\\d+)votes" ) ) );
                wordExample.setAnswers( BasicNumberUtil.getNumber( PatternUtil
                        .getFirstPatternGroup( text, "(\\d+)answers" ) ) );

                boolean isMatched = isMatchedQuestion( wordExample,
                        fromDbflag );
                if ( isMatched )
                {
                    examples.add( wordExample );
                }

            }
        } catch ( Exception e )
        {
            throw new CheckException( e );
        }
        return examples;
    }

    private boolean isMatchedQuestion( WordExample wordExample,
            boolean fromDbflag )
    {
        boolean isMatched = false;
        String q = wordExample.getQ();
        String excerpt = wordExample.getExcerpt();
        if ( q.contains( word ) || excerpt.contains( word ) )
        {
            wordExample
                    .setExcerpt( clearHtmlText( excerpt, word, fromDbflag ) );
            wordExample.setQ( clearHtmlText( q, word, fromDbflag ) );
            isMatched = true;
        } else if ( !word.equalsIgnoreCase( orginalWord )
                && (q.contains( orginalWord )
                        || excerpt.contains( orginalWord )) )
        {
            wordExample.setExcerpt(
                    clearHtmlText( excerpt, orginalWord, fromDbflag ) );
            wordExample.setQ( clearHtmlText( q, orginalWord, fromDbflag ) );
            isMatched = true;
        }
        return isMatched;
    }

    private String clearHtmlText( String text, String replaceMent,
            boolean fromDbflag )
    {
        if ( !fromDbflag )
        {
            text = text.replaceAll( "<.*?>", "" );
            text = StringEscapeUtils.escapeHtml( text );
        }
        return text.replace( replaceMent,
                "<b class='word-highlight'>" + replaceMent + "</b>" );
    }

    private void initResultCount( Document document )
    {
        resultCount = BasicNumberUtil
                .getNumber( PatternUtil.getFirstPatternGroup(
                        document.select( ".subheader.results-header h2" ).text()
                                .replaceAll( "[\\s\\,]", "" ),
                        "\\d+" ) );

    }

}