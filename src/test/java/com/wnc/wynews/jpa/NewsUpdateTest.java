
package com.wnc.wynews.jpa;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wnc.sboot1.spy.wy.WyNewsUpdateSpy;
import com.wnc.string.PatternUtil;
import com.wnc.tools.FileOp;
import com.wnc.wynews.jpa.entity.WyNews;
import com.wnc.wynews.jpa.repo.WyNewsKeyWordRepository;
import com.wnc.wynews.jpa.repo.WyNewsRepository;
import com.wnc.wynews.model.News;
import com.wnc.wynews.service.WyDbService;
import com.wnc.wynews.utils.WyNewsUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NewsUpdateTest {
    @Autowired
    private WyNewsUpdateSpy wyNewsUpdateSpy;
    @Autowired
    private WyDbService wyDbService;

    @Test
    public void testUpdateNews() throws IOException, InterruptedException {
        wyNewsUpdateSpy.spy();
    }

    @Test
    public void update(){
//        WyDbService wyDbService = WyNewsUtil.getWyDbService();
        WyNews wyNews = new WyNews();
        wyNews.setCmtCount(1);
        wyNews.setCode("DQUBOSGH00038FO9");
        wyDbService.updateNews(wyNews);
    }
}
