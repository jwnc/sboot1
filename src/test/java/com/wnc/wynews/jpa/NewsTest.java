
package com.wnc.wynews.jpa;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wnc.string.PatternUtil;
import com.wnc.tools.FileOp;
import com.wnc.wynews.consts.WyConsts;
import com.wnc.wynews.jpa.entity.WyNews;
import com.wnc.wynews.jpa.repo.WyNewsKeyWordRepository;
import com.wnc.wynews.jpa.repo.WyNewsRepository;
import com.wnc.wynews.model.Comment;
import com.wnc.wynews.model.News;
import com.wnc.wynews.model.User;
import com.wnc.wynews.service.WyDbService;
import com.wnc.wynews.utils.WyNewsUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NewsTest {
    @Autowired
    private WyNewsKeyWordRepository wyNewsKeyWordRepository;

    @Test
    public void testNewsAll() {
        String folder = "C:\\data\\spider\\netease-news\\news-list";
        for (File f : new File(folder).listFiles()) {
            String filePath = f.getAbsolutePath();
            seekEachFile(filePath);
        }
    }

    private void testFindOne() {
        System.out.println(wyNewsKeyWordRepository.findOne("t恤232423"));
    }

    private void seekEachFile(String filePath) {
        WyDbService wyDbService = WyNewsUtil.getWyDbService();

        String moduleName = PatternUtil.getLastPatternGroup(filePath, "([^\\\\]+)\\.");
        List<String> newsStrs = FileOp.readFrom(filePath);
        for (String s : newsStrs) {
            JSONObject obj = JSONObject.parseObject(s);
            JSONArray keywords = obj.getJSONArray("keywords");
            if (keywords != null && keywords.size() > 0) {
                checkKws(obj, keywords);
            }
            News news = obj.toJavaObject(News.class);
            System.out.println(news.getDocurl());
            WyNews wyNews = EntityConvertor.newsToEntity(moduleName, news);
            wyDbService.singleNews(wyNews);
        }
    }

    private void checkKws(JSONObject obj, JSONArray keywords) {
        JSONObject jsonObject = keywords.getJSONObject(0);
        if (jsonObject.keySet().size() == 0) {
            obj.remove("keywords");
        } else {
            convertKws(keywords);
        }
    }

    private void convertKws(JSONArray keywords) {
        for (int i = 0; i < keywords.size(); i++) {
            JSONObject jsonObject1 = keywords.getJSONObject(i);
            if (jsonObject1.containsKey("name")) {
                String name = jsonObject1.getString("name");
                jsonObject1.put("keyname", name);
                jsonObject1.remove("name");

                if (jsonObject1.containsKey("url")) {
                    jsonObject1.put("akey_link", jsonObject1.getString("url"));
                    jsonObject1.remove("url");
                }
            }
        }
    }
}
