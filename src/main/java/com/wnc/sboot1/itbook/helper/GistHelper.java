package com.wnc.sboot1.itbook.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wnc.basic.BasicFileUtil;
import com.wnc.sboot1.itbook.MyAppParams;
import com.wnc.sboot1.itbook.service.ITBookLogService;
import com.wnc.string.PatternUtil;

/**
 * gist时间用的是标准格林威治时间, 比北京时间晚8小时.(加上8小时就是北京时间)
 * 
 * @author wWX452950
 */
public class GistHelper
{
    final static Logger logger = Logger.getLogger( GistHelper.class );

    public GistHelper()
    {

    }

    public void checkGist()
    {
        try
        {
            String str = getJsonStr();
            JSONArray arr = JSONObject.parseArray( str );
            for ( int i = 0; i < arr.size(); i++ )
            {
                JSONObject jsonObject = arr.getJSONObject( i );
                JSONObject filesJObj = jsonObject.getJSONObject( "files" );

                String gistName = getGistFileNodeKey( filesJObj );
                String updated = jsonObject.getString( "updated_at" );
                String cvtToBjTime = TimeUtils.cvtToBjTime( updated );
                if ( gistName != null )
                {
                    logger.info( "find gist【" + gistName
                            + "】 updated at beijing time:" + cvtToBjTime );
                    String gistUrl = filesJObj.getJSONObject( gistName )
                            .getString( "raw_url" );
                    if ( !BookLogRetrieving.retrieved( "GIST", cvtToBjTime ) )
                    {
                        if ( gistDownloader( gistName, gistUrl ) )
                        {
                            BookLogRetrieving.log( "GIST", PatternUtil
                                    .getLastPattern( gistName, "\\d+\\.txt" ),
                                    cvtToBjTime );
                            ITBookLogService.parseLogsToDb(
                                    getDestPath( gistName ), "GIST" );
                        }
                    } else
                    {
                        logger.info( gistName + " 已经下载过!" );
                    }
                }
            }
        } catch ( Exception e )
        {
            logger.error( "get gists error:" + e.toString() );
            e.printStackTrace();
            throw new RuntimeException( e );
        }
    }

    private String getJsonStr() throws IOException
    {
        String url = "https://api.github.com/users/wtjavaer88/gists";
        String str = Jsoup.connect( url )
        // .proxy( "openproxy.huawei.com", 8080 )
                .ignoreContentType( true ).timeout( 5000 ).execute().body();
        return str;
    }

    /**
     * 记录gist时的文件名, 格式为itbook-20171014.txt
     * 
     * @param gistName
     *            20171014.txt
     * @param gistUrl
     *            http://***.txt
     * @param destPath
     *            D:/***20171014_031409.txt
     */
    private boolean gistDownloader( String gistName, String gistUrl )
    {
        logger.info( "start download gist【" + gistName + "】 。。。。。。" );
        String destPath = getDestPath( gistName );
        try
        {
            if ( BasicFileUtil.isExistFile( destPath ) )
            {
                BasicFileUtil.CopyFile( destPath, destPath + ".bak" );
            }
            netFileDownload( gistUrl, destPath );

            logger.info( "end download gist【" + gistName + "】 。。。。。。 to 【"
                    + destPath + "】" );

            return true;
        } catch ( Exception e )
        {
            if ( BasicFileUtil.getFileSize( destPath ) == 0 )
            {
                BasicFileUtil.deleteFile( destPath );
                if ( BasicFileUtil.isExistFile( destPath + ".bak" ) )
                {
                    BasicFileUtil.CopyFile( destPath + ".bak", destPath );
                }
            }
            logger.error( " download gist【" + gistUrl + "】  error:"
                    + e.toString() );
        }
        return false;
    }

    private void netFileDownload( String gistUrl, String destPath )
            throws Exception
    {
        FileOutputStream out;
        Response file = (Response)Jsoup.connect( gistUrl )
                .ignoreContentType( true ).method( Method.GET ).execute();
        int len = file.bodyAsBytes().length;
        out = new FileOutputStream( new File( destPath ) );
        out.write( file.bodyAsBytes(), 0, len );
        out.close();
    }

    private String getDestPath( String gistName )
    {
        // gistName处理为itbook日志通用格式
        String logName = PatternUtil.getLastPattern( gistName, "\\d+\\.txt" );
        String destPath = MyAppParams.getInstance().getGistFolder() + logName;
        return destPath;
    }

    /**
     * 获取json中的itbook文件节点名
     * 
     * @param filesJObj
     * @return
     */
    private String getGistFileNodeKey( JSONObject filesJObj )
    {
        for ( Entry<String, Object> entry : filesJObj.entrySet() )
        {
            String key = entry.getKey();
            if ( key.matches( "itbook\\-\\d+.txt" ) )
            {
                return key;
            }
        }
        return null;
    }
}
