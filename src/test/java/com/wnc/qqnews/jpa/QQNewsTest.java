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
        String qqNewsStr = "{\"comment_num\":2,\"img\":\"http://inews.gtimg.com/newsapp_ls/0/4720121314_240180/0\",\"flag\":0,\"category_chn\":\"社会\",\"img_count\":4,\"keywords\":\"大一新生;校园贷1\",\"source_fans\":0,\"source\":\"占星小哥哥\",\"fm_url\":\"\",\"title\":\"大一新生要注意的4件事，自己长点心，别让他们把你套路了\",\"irs_imgs\":{\"273X145\":[\"http://img1.gtimg.com/rcdimg/20180807/17/2336214593_273x145.jpg\",\"http://img1.gtimg.com/rcdimg/20180807/17/1404129868_273x145.jpg\",\"http://img1.gtimg.com/rcdimg/20180807/17/3519710968_273x145.jpg\",\"http://img1.gtimg.com/rcdimg/20180807/17/2880204814_273x145.jpg\"],\"966X604\":[\"http://img1.gtimg.com/rcdimg/20180807/17/3548239273_966x604.jpg\",\"http://img1.gtimg.com/rcdimg/20180807/17/2903253009_966x604.jpg\"],\"120X90\":[\"http://img1.gtimg.com/rcdimg/20180807/17/2316941362_120x90.jpg\",\"http://img1.gtimg.com/rcdimg/20180807/17/1396398759_120x90.jpg\",\"http://img1.gtimg.com/rcdimg/20180807/17/3505026662_120x90.jpg\",\"http://img1.gtimg.com/rcdimg/20180807/17/2857194249_120x90.jpg\"],\"276X194\":[\"http://img1.gtimg.com/rcdimg/20180807/17/2348855509_276x194.jpg\",\"http://img1.gtimg.com/rcdimg/20180807/17/1416645864_276x194.jpg\",\"http://img1.gtimg.com/rcdimg/20180807/17/3553640746_276x194.jpg\",\"http://img1.gtimg.com/rcdimg/20180807/17/2913136075_276x194.jpg\"],\"990X477\":[\"http://img1.gtimg.com/rcdimg/20180807/17/3572537139_990x477.jpg\",\"http://img1.gtimg.com/rcdimg/20180807/17/2932820863_990x477.jpg\"],\"227X148\":[\"http://img1.gtimg.com/rcdimg/20180807/17/2324773736_227x148.jpg\",\"http://img1.gtimg.com/rcdimg/20180807/17/1401760704_227x148.jpg\",\"http://img1.gtimg.com/rcdimg/20180807/17/3513780282_227x148.jpg\",\"http://img1.gtimg.com/rcdimg/20180807/17/2875204889_227x148.jpg\"],\"340X155\":[\"http://img1.gtimg.com/rcdimg/20180807/17/2342407173_340x155.jpg\",\"http://img1.gtimg.com/rcdimg/20180807/17/1408662497_340x155.jpg\",\"http://img1.gtimg.com/rcdimg/20180807/17/3532886045_340x155.jpg\",\"http://img1.gtimg.com/rcdimg/20180807/17/2885730449_340x155.jpg\"],\"328X231\":[\"http://img1.gtimg.com/rcdimg/20180807/17/2361952354_328x231.jpg\",\"http://img1.gtimg.com/rcdimg/20180807/17/1435686307_328x231.jpg\",\"http://img1.gtimg.com/rcdimg/20180807/17/3558709111_328x231.jpg\",\"http://img1.gtimg.com/rcdimg/20180807/17/2920923679_328x231.jpg\"]},\"vurl\":\"http://new.qq.com/omn/20180807/20180807A132US00\",\"bimg\":\"http://inews.gtimg.com/newsapp_ls/0/4720121314_240180/0\",\"duration\":0,\"update_time\":\"2018-08-07 16:14:10\",\"category_id\":\"37\",\"publish_time\":\"2018-08-07 16:14:10\",\"title_len\":25.666666,\"intro\":\"大一新生要注意的4件事，自己长点心，别让他们把你套路了\",\"from\":\"kuaibao\",\"s_group\":0,\"id\":\"20180807A132US\",\"app_id\":\"20180807A132US00\",\"category2_id\":\"\",\"pool_type\":\"irs:article\",\"ext\":{\"tonality_score\":0,\"news_score\":3,\"img_type\":0},\"multi_imgs\":[\"http://inews.gtimg.com/newsapp_bt/0/4720117411/641\",\"http://inews.gtimg.com/newsapp_bt/0/4720117410/641\",\"http://inews.gtimg.com/newsapp_bt/0/4720117413/641\"],\"imgs\":{\"273X145\":\"http://img1.gtimg.com/rcdimg/20180807/17/2336214593_273x145.jpg\",\"966X604\":\"http://img1.gtimg.com/rcdimg/20180807/17/3548239273_966x604.jpg\",\"120X90\":\"http://img1.gtimg.com/rcdimg/20180807/17/2316941362_120x90.jpg\",\"276X194\":\"http://img1.gtimg.com/rcdimg/20180807/17/2348855509_276x194.jpg\",\"990X477\":\"http://img1.gtimg.com/rcdimg/20180807/17/3572537139_990x477.jpg\",\"227X148\":\"http://img1.gtimg.com/rcdimg/20180807/17/2324773736_227x148.jpg\",\"340X155\":\"http://img1.gtimg.com/rcdimg/20180807/17/2342407173_340x155.jpg\",\"328X231\":\"http://img1.gtimg.com/rcdimg/20180807/17/2361952354_328x231.jpg\"},\"article_type\":0,\"play_url_small\":\"\",\"surl\":\"\",\"comment_id\":\"2961995585\",\"mini_img\":\"\",\"url\":\"https://xw.qq.com/cmsid/20180807A132US00\",\"tags\":\"大一新生;校园贷\",\"category1_id\":\"37\",\"category2_chn\":\"\",\"play_url_medium\":\"\",\"play_url_high\":\"\",\"source_id\":\"8192885\",\"category\":\"society\",\"strategy\":2,\"category1_chn\":\"社会\",\"tag_label\":[[\"大一新生1\",\"358957\"],[\"校园贷\",\"235125\"]],\"view_count\":640,\"source_logo\":\"\",\"ts\":1533629651}";
//        QqNews qqNews = JSONObject.parseObject(qqNewsStr, QqNews.class);
//        QqNews x = qqNews.cvtAll();
//        System.out.println(x);
        qqDbService.singleNews(JSONObject.parseObject(qqNewsStr));
    }

    @Test
    public void t2() {
        QqDbService qqDbService = (QqDbService) SpringContextUtils.getContext().getBean("qqDbService");

        for (File f : new File("C:\\data\\spider\\qq-news\\news-list\\").listFiles()) {
            List<String> strings = FileOp.readFrom(f.getAbsolutePath());
            for (String s : strings) {
                //QqNews qqNews = JSONObject.parseObject(s, QqNews.class);
                //QqNews x = qqNews.cvtAll();
                qqDbService.singleNews(JSONObject.parseObject(s));
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
