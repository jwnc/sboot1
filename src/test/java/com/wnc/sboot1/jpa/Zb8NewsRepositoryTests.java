
package com.wnc.sboot1.jpa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.wnc.sboot1.spy.zuqiu.rep.HotCommentRepository;
import com.wnc.sboot1.spy.zuqiu.rep.Zb8NewsRepository;

@RunWith( SpringRunner.class )
@SpringBootTest
public class Zb8NewsRepositoryTests
{

    @Autowired
    private Zb8NewsRepository zb8NewsRepository;
    @Autowired
    private HotCommentRepository hotCommentRepository;

    @Test
    public void test() throws Exception
    {
        // Zb8News zb8News = new Zb8News();
        // zb8News.setUrl( "111" );
        // zb8NewsRepository.save( zb8News );
        // Assert.assertEquals( 1, zb8NewsRepository.findAll().size() );
        //
        // zb8NewsRepository.deleteByUrl( "111" );

        // HotComment cmt = new HotComment();
        // cmt.setId( 99 );
        // hotCommentRepository.save( cmt );

        // hotCommentRepository.deleteByFilename( "2018_04_21-news-nba-121644"
        // );

    }

}