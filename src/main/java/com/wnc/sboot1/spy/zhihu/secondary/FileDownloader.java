
package com.wnc.sboot1.spy.zhihu.secondary;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

import com.crawl.core.util.HttpClientUtil;

public class FileDownloader
{

    public static void downloadFile( HttpRequestBase requestBase,
            String savePath ) throws Exception
    {

        CloseableHttpResponse response;
        OutputStream os = null;
        InputStream is = null;
        try
        {
            response = HttpClientUtil.getResponse( requestBase );
            System.out.println(
                    "status:" + response.getStatusLine().getStatusCode() );
            File file = new File( savePath );

            // 如果文件不存在，则下载

            os = new FileOutputStream( file );
            is = response.getEntity().getContent();
            byte[] buff = new byte[(int)response.getEntity()
                    .getContentLength()];
            while ( true )
            {
                int readed = is.read( buff );
                if ( readed == -1 )
                {
                    break;
                }
                byte[] temp = new byte[readed];
                System.arraycopy( buff, 0, temp, 0, readed );
                os.write( temp );
            }
        } catch ( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException( e.toString() );
        } finally
        {
            if ( is != null )
            {
                try
                {
                    is.close();
                } catch ( Exception e )
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally
                {
                    if ( os != null )
                        os.close();
                }
            }
        }

        System.out.println( requestBase.getURI() + "--文件成功下载至" + savePath );
        response.close();

    }
}
