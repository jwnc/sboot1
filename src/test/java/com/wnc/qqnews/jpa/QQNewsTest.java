package com.wnc.qqnews.jpa;

import com.alibaba.fastjson.JSONObject;
import com.wnc.qqnews.jpa.entity.QqNews;
import com.wnc.qqnews.service.QqDbService;
import com.wnc.sboot1.SpringContextUtils;
import com.wnc.tools.FileOp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QQNewsTest {
    @Test
    public void t1() {
        QqDbService qqDbService = (QqDbService) SpringContextUtils.getContext().getBean("qqDbService");
        String qqNewsStr = "{\"comment_num\":8,\"img\":\"http://inews.gtimg.com/newsapp_ls/0/4430163850_870492/0\",\"flag\":0,\"category_chn\":\"文化\",\"img_count\":12,\"keywords\":\"英语;文学\",\"source_fans\":0,\"source\":\"腾讯大家\",\"fm_url\":\"\",\"title\":\"大家 | 教你如何不读书\",\"irs_imgs\":{\"273X145\":[\"http://img1.gtimg.com/rcdimg/20180718/15/2668215983_273x145.jpg\",\"http://img1.gtimg.com/rcdimg/20180718/15/2511545247_273x145.jpg\",\"http://img1.gtimg.com/rcdimg/20180718/15/2671349868_273x145.jpg\",\"http://img1.gtimg.com/rcdimg/20180718/15/3076403724_273x145.jpg\"],\"966X604\":[\"http://img1.gtimg.com/rcdimg/20180718/15/2688360928_966x604.jpg\",\"http://img1.gtimg.com/rcdimg/20180718/15/2522229710_966x604.jpg\",\"http://img1.gtimg.com/rcdimg/20180718/15/2698659578_966x604.jpg\",\"http://img1.gtimg.com/rcdimg/20180718/15/3105192505_966x604.jpg\"],\"120X90\":[\"http://img1.gtimg.com/rcdimg/20180718/15/2632761673_120x90.jpg\",\"http://img1.gtimg.com/rcdimg/20180718/15/2496672186_120x90.jpg\",\"http://img1.gtimg.com/rcdimg/20180718/15/2652539702_120x90.jpg\",\"http://img1.gtimg.com/rcdimg/20180718/15/3037102433_120x90.jpg\"],\"276X194\":[\"http://img1.gtimg.com/rcdimg/20180718/15/2702893307_276x194.jpg\",\"http://img1.gtimg.com/rcdimg/20180718/15/2543729254_276x194.jpg\",\"http://img1.gtimg.com/rcdimg/20180718/15/2707093165_276x194.jpg\",\"http://img1.gtimg.com/rcdimg/20180718/15/3112236026_276x194.jpg\"],\"990X477\":[\"http://img1.gtimg.com/rcdimg/20180718/15/2720332327_990x477.jpg\",\"http://img1.gtimg.com/rcdimg/20180718/15/2564928256_990x477.jpg\",\"http://img1.gtimg.com/rcdimg/20180718/15/2728907266_990x477.jpg\",\"http://img1.gtimg.com/rcdimg/20180718/15/3145354687_990x477.jpg\"],\"227X148\":[\"http://img1.gtimg.com/rcdimg/20180718/15/2659802297_227x148.jpg\",\"http://img1.gtimg.com/rcdimg/20180718/15/2500340698_227x148.jpg\",\"http://img1.gtimg.com/rcdimg/20180718/15/2663401592_227x148.jpg\",\"http://img1.gtimg.com/rcdimg/20180718/15/3069259051_227x148.jpg\"],\"340X155\":[\"http://img1.gtimg.com/rcdimg/20180718/15/2675566362_340x155.jpg\",\"http://img1.gtimg.com/rcdimg/20180718/15/2517114871_340x155.jpg\",\"http://img1.gtimg.com/rcdimg/20180718/15/2679818570_340x155.jpg\",\"http://img1.gtimg.com/rcdimg/20180718/15/3090721177_340x155.jpg\"],\"328X231\":[\"http://img1.gtimg.com/rcdimg/20180718/15/2710482461_328x231.jpg\",\"http://img1.gtimg.com/rcdimg/20180718/15/2556323128_328x231.jpg\",\"http://img1.gtimg.com/rcdimg/20180718/15/2714826557_328x231.jpg\",\"http://img1.gtimg.com/rcdimg/20180718/15/3126679039_328x231.jpg\"]},\"vurl\":\"http://new.qq.com/cmsn/20180718/20180718029408.html\",\"bimg\":\"http://inews.gtimg.com/newsapp_ls/0/4430163850_870492/0\",\"duration\":0,\"update_time\":\"2018-07-18 14:29:09\",\"category_id\":\"9\",\"publish_time\":\"2018-07-18 14:29:09\",\"title_len\":10.666667,\"intro\":\"文/陈松（美国哈佛大学东亚语言与文明博士）　我们中国人常常把知识分子称为“读书人”，又把上学做研究叫做“念书”。不过，今天我想谈谈如何“不读书”还能做研究，而且也能取得有趣的研究成果。　做研究虽然要看书，但是没有研究者能看完所有的书。斯坦福大学英语文学系的教授Franco Moretti说，文学作品不能一本一本地读。为什么呢？因为太多了，读不完。他说“光是19世纪的英国就有三万部小说，总共也许有四万、五万、六万部，没人全部读过、今后也没人能全部通读一遍。此外，还有法国小说、中国小说、阿根廷小说、美国小说…\",\"from\":\"cms_pool\",\"s_group\":0,\"id\":\"20180718029408\",\"app_id\":\"CRI2018071802940800\",\"category2_id\":\"911\",\"pool_type\":\"irs:org\",\"ext\":{\"tonality_score\":2,\"news_score\":4,\"img_type\":1},\"multi_imgs\":[\"http://inews.gtimg.com/newsapp_bt/0/4430001454/641\",\"http://inews.gtimg.com/newsapp_bt/0/4430001451/641\",\"http://inews.gtimg.com/newsapp_bt/0/4430001456/641\"],\"imgs\":{\"273X145\":\"http://img1.gtimg.com/rcdimg/20180718/15/2668215983_273x145.jpg\",\"966X604\":\"http://img1.gtimg.com/rcdimg/20180718/15/2688360928_966x604.jpg\",\"120X90\":\"http://img1.gtimg.com/rcdimg/20180718/15/2632761673_120x90.jpg\",\"276X194\":\"http://img1.gtimg.com/rcdimg/20180718/15/2702893307_276x194.jpg\",\"990X477\":\"http://img1.gtimg.com/rcdimg/20180718/15/2720332327_990x477.jpg\",\"227X148\":\"http://img1.gtimg.com/rcdimg/20180718/15/2659802297_227x148.jpg\",\"340X155\":\"http://img1.gtimg.com/rcdimg/20180718/15/2675566362_340x155.jpg\",\"328X231\":\"http://img1.gtimg.com/rcdimg/20180718/15/2710482461_328x231.jpg\"},\"article_type\":0,\"play_url_small\":\"\",\"surl\":\"http://view.inews.qq.com/a/CRI2018071802940805\",\"comment_id\":\"2887593480\",\"mini_img\":\"\",\"url\":\"https://xw.qq.com/orignal/20180718029408/CRI2018071802940800\",\"tags\":\"英语;文学\",\"category1_id\":\"9\",\"category2_chn\":\"文学\",\"play_url_medium\":\"\",\"play_url_high\":\"\",\"source_id\":\"orignal\",\"category\":\"orignal\",\"strategy\":2,\"category1_chn\":\"文化\",\"tag_label\":[[\"英语\",\"90939\"],[\"文学\",\"110038\"]],\"view_count\":3,\"source_logo\":\"\",\"ts\":1531895349}";
//        QqNews qqNews = JSONObject.parseObject(qqNewsStr, QqNews.class);
//        QqNews x = qqNews.cvtAll();
//        System.out.println(x);
        qqDbService.singleNews(JSONObject.parseObject(qqNewsStr));
    }

    @Test
    public void t2() {
        for (File f : new File("C:\\data\\spider\\qq-news\\news-list\\").listFiles()) {
            List<String> strings = FileOp.readFrom(f.getAbsolutePath());
            for (String s : strings) {
                QqNews qqNews = JSONObject.parseObject(s, QqNews.class);
                QqNews x = qqNews.cvtAll();
                System.out.println(x);
            }
        }
    }

    @Test
    public void t3() {
        QqDbService qqDbService = (QqDbService) SpringContextUtils.getContext().getBean("qqDbService");
        List<String> strings = FileOp.readFrom("C:\\data\\spider\\qq-news\\news-list\\教育.txt");
        for (String s : strings) {
            qqDbService.singleNews(JSONObject.parseObject(s));
        }
    }
}
