
package com.wnc.dmm;

import com.wnc.basic.BasicFileUtil;
import com.wnc.basic.BasicStringUtil;

public class DmmConsts
{
    // 设置项目路径
    public static String APP_DIR = "C:\\data\\spider\\dmm\\";

    public final static String DETAIL_DIR = APP_DIR + "detail\\";
    public final static String LOGS_DIR = APP_DIR + "logs\\";
    public final static String TEST_DIR = APP_DIR + "test\\";

    public final static String CID_LOG = LOGS_DIR + "cid.txt";

    public final static String VIDEO_LOGS_DIR = LOGS_DIR + "video\\";

    public final static String VIDEO_ALL_LOG = VIDEO_LOGS_DIR
            + "videos-all.txt";
    public final static String VIDEO_HIGH_LOG = VIDEO_LOGS_DIR
            + "videos-high.txt";
    public final static String VIDEO_LOW_LOG = VIDEO_LOGS_DIR
            + "videos-low.txt";

    public final static String SUC_LOG = LOGS_DIR + "succuss.txt";
    public final static String ERR_LOG = LOGS_DIR + "err.txt";

    public final static String PROXY_FILE = APP_DIR + "jp-proxy.txt";

    public final static String MOVIE_PARAMS_URL = "http://www.dmm.co.jp/service/digitalapi/-/html5_player/=/cid=%s/mtype=AhRVShI_/service=litevideo/mode=/width=560/height=360/";
    public static final String HOT_PAGE_URL = "http://www.dmm.co.jp/litevideo/-/list/=/article=keyword/sort=all_ranking/page=%d/";
    public static final String DETAIL_PAGE_URL = "http://www.dmm.co.jp/digital/videoa/-/detail/=/cid=%s/";
    public static final String DETAIL_PAGE_MONO_URL = "http://www.dmm.co.jp/mono/dvd/-/detail/=/cid=%s/";
    public static final String DETAIL_PAGE_C_URL = "http://www.dmm.co.jp/digital/videoc/-/detail/=/cid=%s/";

    public static final String DMM_DOMAIN = "http://www.dmm.co.jp";

    static
    {
        BasicFileUtil.makeDirectory( APP_DIR );
        BasicFileUtil.makeDirectory( DETAIL_DIR );
        BasicFileUtil.makeDirectory( TEST_DIR );
        BasicFileUtil.makeDirectory( LOGS_DIR );
        BasicFileUtil.makeDirectory( VIDEO_LOGS_DIR );

        for ( int i = 0; i < 1000; i++ )
        {
            BasicFileUtil.makeDirectory( DETAIL_DIR
                    + BasicStringUtil.fillLeftString( "" + i, 3, "0" ) );
        }
    }
}
