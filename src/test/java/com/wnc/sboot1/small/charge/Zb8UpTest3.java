
package com.wnc.sboot1.small.charge;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.wnc.sboot1.spy.zuqiu.Zb8CommentBatchUtils;

@RunWith( SpringRunner.class )
@SpringBootTest
public class Zb8UpTest3
{
    @Autowired
    private Zb8CommentBatchUtils zb8CommentBatchUtils;

    // @Test
    public void tt() throws Exception
    {
        zb8CommentBatchUtils.init();
        zb8CommentBatchUtils.upById(
                "https://news.zhibo8.cc/nba/2017-01-13/5878366aea766.htm",
                44277263, 180 );

        Thread.sleep( 1000 * 3600L );
    }

    // C罗欧洲杯， 我的评论
    // @Test
    public void saveT() throws IOException
    {
        zb8CommentBatchUtils.init();
        zb8CommentBatchUtils.upById(
                "https://www.zhibo8.cc/zuqiu/2018/0614-49fc5b8-svideo.htm",
                109815122, 10 );
    }

    // 梅西 非洲雄鹰
    // @Test
    public void saveTs() throws IOException
    {
        zb8CommentBatchUtils.init();
        zb8CommentBatchUtils.upById(
                "https://news.zhibo8.cc/zuqiu/2018-06-14/5b21edb1c7b68.htm",
                109818729, 50 );
    }

    @Test
    public void saveTss() throws IOException
    {
        zb8CommentBatchUtils.init();
        zb8CommentBatchUtils.downById(
                "https://www.zhibo8.cc/nba/2018/0621-Duncan.htm", 110824947,
                150 );
    }
}