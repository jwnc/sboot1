
package com.wnc.wynews.utils;

import org.apache.commons.lang.StringUtils;

public class NewsPageUrlGenerator
{
    /**
     * @Description 根据模块生成pageIdx页的地址
     * @Date 2018/7/26 10:48
     * @Param more 指定的加载连接
     * @Param pageIdx
     * @Return java.lang.String
     */
    public static String generatorPageUrl( final String more, int pageIdx )
    {
        if ( StringUtils.isBlank( more ) )
        {
            throw new RuntimeException( "more link should not be null." );
        }
        String ret = more;
        final String reg = "(_%2d)";
        if ( more.contains( reg ) )
        {
            if ( pageIdx == 1 )
            {
                ret = more.replace( reg, "" );
            } else
            {
                ret = more.replace( reg,
                        "_" + (pageIdx > 9 ? "" : "0") + pageIdx );
            }
        } else if ( more.startsWith( "https://pacaio.match.qq.com" ) )
        {
            ret = String.format( more, pageIdx );
        }
        return ret;
    }

    public static void main( String[] args )
    {
        System.out.println( generatorPageUrl(
                "http://tech.163.com/special/00097UHL/smart_datalist(_%2d).js",
                1 ) );
    }
}
