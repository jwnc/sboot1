
package com.wnc.sboot1.itbook.helper;

import java.io.File;
import java.io.FileInputStream;

import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;

public class UploadUtil
{
    public static void main( String[] args ) throws Exception
    {
        uploadBookLog();
    }

    private static void uploadBookLog()
    {
        String ipport = "http://122.112.219.62:8080/sboot1";
        ipport = "http://localhost:8080";
        String day = "20171130";
        try
        {
            File file = new File( "D:/itbook/log/" + day + ".txt" );
            if ( !file.exists() )
            {
                System.out.println( "找不到上传文件-" + day + ".txt" );
                return;
            }
            String post = Jsoup.connect( ipport + "/upload/txt" )
                    .data( "file", file.getName(), new FileInputStream( file ) )
                    .data( "client", getPCName() ).method( Method.POST )
                    .ignoreContentType( true ).execute().body();
            System.out.println( post );
            if ( post.contains( "成功!" ) )
            {
                System.out.println( "上传成功!" );
            } else
            {
                System.out.println( "上传失败!" );
            }
        } catch (

        Exception e )
        {
            e.printStackTrace();
        }
    }

    private static String getPCName()
    {
        return System.getenv().get( "COMPUTERNAME" );
    }
}