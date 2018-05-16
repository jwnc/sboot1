
package com.wnc.sboot1.jpa;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSONObject;
import com.wnc.sboot1.spy.helper.ZhihuActivityHelper;
import com.wnc.sboot1.spy.zhihu.active.Activity;
import com.wnc.tools.FileOp;

@RunWith( SpringRunner.class )
@SpringBootTest
public class ActTest
{
    private static Logger logger = Logger.getLogger( ActTest.class );
    @Autowired
    private ZhihuActivityHelper zhihuActivityHelper;

    @Test
    public void a() throws Exception
    {
        File[] listFiles = new File( "D:\\个人工作\\spy\\zhihu\\activities" )
                .listFiles();
        for ( File file : listFiles )
        {
            testTarget( file );
        }
    }

    private void testTarget( File file ) throws Exception
    {
        System.out.println( file.getName() );
        List<String> readFrom = FileOp.readFrom( file.getAbsolutePath() );
        String ret = "";
        for ( String string : readFrom )
        {
            ret += string;
        }
        JSONObject parseObject = JSONObject.parseObject( ret );
        String data = parseObject.getString( "data" );
        if ( data == null || data.length() < 10 )
        {
            return;
        }
        List<Activity> parseArray = JSONObject.parseArray( data,
                Activity.class );
        zhihuActivityHelper.save( parseArray );
    }
}
