
package com.wnc.wynews.jpa;

import com.wnc.sboot1.SpringContextUtils;
import com.wnc.wynews.jpa.entity.WyNews;
import com.wnc.wynews.jpa.entity.WyNewsKeyword;
import com.wnc.wynews.jpa.repo.WyNewsKeyWordRepository;
import com.wnc.wynews.jpa.repo.WyNewsRepository;
import com.wnc.wynews.service.WyDbService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JpaTest {
    @Autowired
    private WyNewsRepository wyNewsRepository;

    @Autowired
    private WyNewsKeyWordRepository wyNewsKeyWordRepository;

    //    @Test
    public void d() throws IOException, InterruptedException {

        WyNews wyNews = new WyNews();
        wyNews.setCmtCount(11);
        wyNews.setCommenturl("cmtUrl");
        wyNews.setDigest("digest");
        wyNews.setDocurl("docurl");
        wyNews.setImgurl("imgurl");
        wyNews.setLabel("label");
        wyNews.setTime("2018-01-01 11:11:00");
        wyNews.setTitle("Title");
        wyNews.setCode("Code2");
        wyNews.setModule("国内新闻");

        WyNewsKeyword wyNewsKeyWord = new WyNewsKeyword();
        wyNewsKeyWord.setName("滴滴2");
        wyNewsKeyWord.setLink("滴滴url2");
        WyNewsKeyword wyNewsKeyWord1 = new WyNewsKeyword();
        wyNewsKeyWord1.setName("滴滴");
        wyNewsKeyWord1.setLink("滴滴url1");
        wyNewsKeyWordRepository.save(wyNewsKeyWord);
        wyNewsKeyWordRepository.save(wyNewsKeyWord1);

        wyNews.setWyNewsKeywords(new HashSet(Arrays.asList(wyNewsKeyWord, wyNewsKeyWord, wyNewsKeyWord, wyNewsKeyWord1)));
        wyNewsRepository.save(wyNews);
    }

    /**
     * 测试从new出来的对象中获取bean
     *
     * @param
     * @return void
     * @since 2018/8/29 10:48
     */
    @Test
    public void a() {
        System.out.println(new TTT().getWy().hashCode());
    }

    public static class TTT {
        public WyDbService getWy() {
            WyDbService wyHelper = (WyDbService) SpringContextUtils.getContext().getBean("wyDbService");
            System.out.println(wyHelper.hashCode());
            WyDbService wyHelper2 = (WyDbService) SpringContextUtils.getContext().getBean("wyDbService");
            System.out.println(wyHelper2.hashCode());
            return wyHelper;
        }
    }
}
