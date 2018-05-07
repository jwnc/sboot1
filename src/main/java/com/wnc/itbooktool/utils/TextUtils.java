package com.wnc.itbooktool.utils;

import com.wnc.string.PatternUtil;
import com.wnc.tools.TextFormatUtil;

public class TextUtils
{
    public static boolean isSequence( String clipBoardContent )
    {
        int size = PatternUtil.getAllPatternGroup( clipBoardContent,
                "([a-zA-Z\\-_']+)" ).size();
        return !clipBoardContent.startsWith( "http" )
                && !TextFormatUtil.containsChinese( clipBoardContent )
                && size > 1 && size < 50;
    }

    public static boolean isSingleWord( String clipBoardContent )
    {
        return clipBoardContent.matches( "[a-zA-Z]+" );
    }
}