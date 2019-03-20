
package com.wnc.itbooktool.word;

import org.springframework.beans.factory.annotation.Autowired;

import com.wnc.itbooktool.dao.DictionaryDao;
import com.wnc.string.PatternUtil;

public class WordUtils
{
	@Autowired
	private DictionaryDao dictionaryDao;
	
    public  DicWord findFromCache( String clipBoardContent )
    {
        for ( DicWord d : OptedDictData.getSeekWordList() )
        {
            if ( d != null && hasFindWord( clipBoardContent, d ) )
            {
                return d;
            }
        }
        return null;
    }

    private  boolean hasFindWord( String clipBoardContent, DicWord d )
    {
        return clipBoardContent.equalsIgnoreCase( d.getBase_word() )
                || clipBoardContent.equalsIgnoreCase( d.getWord_done() )
                || clipBoardContent.equalsIgnoreCase( d.getWord_er() )
                || clipBoardContent.equalsIgnoreCase( d.getWord_est() )
                || clipBoardContent.equalsIgnoreCase( d.getWord_ing() )
                || clipBoardContent.equalsIgnoreCase( d.getWord_past() )
                || clipBoardContent.equalsIgnoreCase( d.getWord_pl() )
                || clipBoardContent.equalsIgnoreCase( d.getWord_third() );
    }

    public  DicWord findDicWord( String clipBoardContent )
    {
        DicWord fw = findFromCache( clipBoardContent );
        if ( fw == null )
        {
            fw = dictionaryDao.findWeightWord( clipBoardContent );
        }
        if ( fw == null )
        {
            fw = dictionaryDao.findWord( clipBoardContent );
        }
        return fw;
    }

    /**
     * 判断句子是否为上下文单词相关
     * 
     * @param clipboardString
     * @return
     */
    public  boolean isContextRelative( String clipboardString )
    {
        clipboardString = clipboardString.trim();
        for ( String word : PatternUtil.getAllPatternGroup( clipboardString,
                "([a-zA-Z]+)" ) )
        {
            for ( DicWord d : OptedDictData.getSeekWordList() )
            {
                if ( d != null && hasFindWord( word, d ) )
                {
                    return true;
                }
            }
        }
        return false;
    }
}