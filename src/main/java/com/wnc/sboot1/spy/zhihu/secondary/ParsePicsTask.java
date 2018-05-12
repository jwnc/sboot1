
package com.wnc.sboot1.spy.zhihu.secondary;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawl.spider.SpiderHttpClient;
import com.wnc.basic.BasicFileUtil;
import com.wnc.sboot1.spy.util.ProxyProcess;
import com.wnc.string.PatternUtil;
import com.wnc.tools.FileOp;

public class ParsePicsTask
{
    public static final String ERR_LOG = "D:\\个人工作\\spy\\zhihu\\topics\\表情图\\err.log";
    public static final String ALLPICS_LOG = "D:\\个人工作\\spy\\zhihu\\topics\\表情图\\all_pics.log";
    @Autowired
    static ProxyProcess proxyProcess;

    @SuppressWarnings( "unchecked" )
    public static void main( String[] args )
    {
        testValid();
        try
        {
            proxyProcess.init();
        } catch ( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String folder = "D:\\个人工作\\spy\\zhihu\\topics\\表情图\\";
        Set<String> pics = new HashSet<String>();
        int cc = 1;
        for ( File file : new File( folder ).listFiles() )
        {
            if ( !file.getName().endsWith( ".json" ) )
            {
                continue;
            }
            JSONArray jsonArray = JSONObject
                    .parseArray( StringUtils
                            .join( FileOp.readFrom( file.getAbsolutePath() ) ) )
                    .getJSONObject( 0 ).getJSONArray( "data" );
            for ( int i = 0; i < jsonArray.size(); i++ )
            {
                String string = jsonArray.getJSONObject( i )
                        .getJSONObject( "target" ).getString( "content" );
                String qtitle = "";
                JSONObject jsonObject = jsonArray.getJSONObject( i )
                        .getJSONObject( "target" ).getJSONObject( "question" );
                if ( jsonObject == null )
                {
                    qtitle = "Others";
                } else
                {
                    qtitle = jsonObject.getString( "title" );
                }
                if ( StringUtils.isBlank( qtitle ) )
                {
                    qtitle = "Others";
                }
                String folderSon = folder + qtitle
                        .replaceAll( "[\\[\\]\"\\'\\<\\>\\*\\?\\:/\\\\]", "" )
                        .trim();
                boolean makeDirectory = BasicFileUtil
                        .makeDirectory( folderSon );
                if ( !makeDirectory )
                {
                    BasicFileUtil.writeFileString( ERR_LOG,
                            qtitle + "无法创建文件夹\r\n", null, true );
                    continue;
                }
                // System.out.println(string);
                List<String> allPatternGroup = PatternUtil
                        .getAllPatternGroup( string, "src=\"(http.*?)\"" );

                for ( String picUrl : allPatternGroup )
                {
                    System.out.println( picUrl );
                    pics.add( picUrl );

                    String filename = folderSon + "\\"
                            + BasicFileUtil.getFileName( picUrl );
                    if ( !BasicFileUtil.isExistFile( filename ) )
                    {
                        SpiderHttpClient.getInstance().getNetPageThreadPool()
                                .execute( new PicDownloadTask( picUrl,
                                        filename ) );
                    }
                    BasicFileUtil.writeFileString( ParsePicsTask.ALLPICS_LOG,
                            picUrl + "\r\n", null, true );
                    cc++;
                }
            }
        }
        System.out.println( pics.size() );
    }

    private static void testValid()
    {
        int cc = 0;
        String folder = "D:\\个人工作\\spy\\zhihu\\topics\\表情图\\";
        for ( File file : new File( folder ).listFiles() )
        {
            if ( !file.getName().endsWith( ".json" )
                    && !file.getName().endsWith( ".log" ) )
            {
                for ( File file2 : file.listFiles() )
                {
                    try
                    {
                        BufferedImage sourceImg = ImageIO
                                .read( new FileInputStream( file2 ) );
                        if ( sourceImg == null || sourceImg.getHeight() == 0 )
                        {
                            System.out.println( (++cc) + "  "
                                    + file2.getAbsolutePath() + " Height = 0" );
                            file2.delete();
                        }
                    } catch ( Exception e )
                    {
                        System.out.println( "Err:" + file2.getAbsolutePath() );
                        BasicFileUtil
                                .writeFileString( ERR_LOG,
                                        file2.getAbsolutePath() + " / "
                                                + e.toString() + " \r\n",
                                        null, true );
                        e.printStackTrace();
                    }
                }
            }
        }

        // try
        // {
        // Thread.sleep( 1000000 );
        // } catch ( InterruptedException e )
        // {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
    }
}
