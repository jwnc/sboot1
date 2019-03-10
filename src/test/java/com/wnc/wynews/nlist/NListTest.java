package com.wnc.wynews.nlist;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wnc.wynews.model.News;
import com.wnc.wynews.model.NewsModule;
import com.wnc.wynews.utils.NewsPageUrlGenerator;
import common.spider.HttpClientUtil;
import org.apache.http.client.methods.HttpGet;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class NListTest {
    @Test
    public void ajax() throws Exception {
        String res = HttpClientUtil.getWebPage(new HttpGet("http://money.163.com/special/002557S5/newsdata_idx_finance.js"), "GBK");
//        System.out.println(res);
        String listStr = res.replaceAll("data_callback\\(|\\)$","");
        System.out.println(listStr);
        JSONArray arr = JSONArray.parseArray(listStr);
        for(int i = 0; i < arr.size(); i++){
            System.out.println(arr.get(i));
        }

        List<News> news = JSONArray.parseArray(listStr, News.class);
        for(News n : news){
            System.out.println(n.getTitle() +" / "+ n.getTime());
        }
    }

    @Test
    public void html() throws Exception {
        String res = HttpClientUtil.getWebPage(new HttpGet("http://mobile.163.com/special/index_datalist_02/"), "GBK");
        System.out.println(res);
    }


    @Test
    public void module(){
        NewsModule module = JSONObject.parseObject("{\n" +
                "    \"url\": \"https://news.163.com/\",\n" +
                "    \"name\": \"网易新闻\",\n" +
                "    \"nodes\":[\n" +
                "        {\n" +
                "            \"url\": \"https://money.163.com/\",\n" +
                "            \"name\": \"财经新闻\",\n" +
                "            \"nodes\":[\n" +
                "               {\"url\": \"https://money.163.com/stock\", \"name\": \"股票\", \"more\":\"http://money.163.com/special/002557S5/newsdata_idx_stock(_%2d).js\"},\n" +
                "               {\"url\": \"https://money.163.com/finance\", \"name\": \"金融\", \"more\":\"http://money.163.com/special/002557S5/newsdata_idx_finance(_%2d).js\"},\n" +
                "               {\"url\": \"https://money.163.com/fund\", \"name\": \"基金\", \"more\":\"http://money.163.com/special/002557S5/newsdata_idx_fund(_%2d).js\"},\n" +
                "               {\"url\": \"https://money.163.com/licai\", \"name\": \"理财\", \"more\":\"http://money.163.com/special/002557S5/newsdata_idx_licai(_%2d).js\"},\n" +
                "               {\"url\": \"http://biz.163.com/\", \"name\": \"商业\", \"more\":\"http://money.163.com/special/002557S5/newsdata_idx_biz(_%2d).js\"},\n" +
                "               {\"url\": \"http://money.163.com/bitcoin\", \"name\": \"比特币\", \"more\":\"http://money.163.com/special/002557S5/newsdata_idx_bitcoin(_%2d).js\"},\n" +
                "               {\"url\": \"http://stock.163.com/\", \"name\": \"易金经\", \"ignore\": \"true\"}\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"url\": \"http://tech.163.com/\",\n" +
                "            \"name\": \"科技新闻\",\n" +
                "            \"nodes\":[\n" +
                "               {\"url\": \"http://mobile.163.com/\", \"name\": \"手机\", \"more\": \"http://mobile.163.com/special/index_datalist(_%2d)/\"},\n" +
                "               {\"url\": \"http://tech.163.com/internet/\", \"name\": \"互联网\", \"more\": \"http://tech.163.com/special/internet_2016(_%2d)/\"},\n" +
                "               {\"url\": \"http://tech.163.com/it/\", \"name\": \"IT\", \"more\": \"http://tech.163.com/special/it_2016(_%2d)/\"},\n" +
                "               {\"url\": \"http://tech.163.com/smart/\", \"name\": \"智能\", \"more\": \"http://tech.163.com/special/00097UHL/smart_datalist(_%2d).js\"}\n" +
                "            ]\n" +
                "        },\n" +
                "\t    {\n" +
                "            \"url\": \"http://sports.163.com/\",\n" +
                "            \"name\": \"体育新闻\",\n" +
                "            \"nodes\":[\n" +
                "               {\"url\": \"http://sports.163.com/nba/\", \"name\": \"NBA\", \"more\": \"http://sports.163.com/special/000587PK/newsdata_nba_index(_%2d).js\"},\n" +
                "               {\"url\": \"http://sports.163.com/world/\", \"name\": \"国际足球\", \"more\": \"http://sports.163.com/special/000587PN/newsdata_world_index(_%2d).js\"},\n" +
                "               {\"url\": \"http://sports.163.com/china/\", \"name\": \"国内足球\", \"more\": \"http://sports.163.com/special/000587PM/newsdata_china_index(_%2d).js\"},\n" +
                "               {\"url\": \"http://sports.163.com/cba/\", \"name\": \"CBA\", \"more\": \"http://sports.163.com/special/000587PL/newsdata_cba_index(_%2d).js\"},\n" +
                "               {\"url\": \"http://sports.163.com/allsports/\", \"name\": \"综合\", \"more\": \"http://sports.163.com/special/000587PQ/newsdata_allsports_index(_%2d).js\"}\n" +
                "            ]\n" +
                "        },\n" +
                "\t    {\n" +
                "            \"url\": \"http://auto.163.com/\",\n" +
                "            \"name\": \"汽车新闻\",\n" +
                "            \"nodes\":[\n" +
                "               {\"url\": \"http://auto.163.com/newcar/\", \"name\": \"新车\", \"more\": \"http://auto.163.com/special/2016nauto(_%2d)/\"},\n" +
                "               {\"url\": \"http://auto.163.com/test/\", \"name\": \"试驾\", \"more\": \"http://auto.163.com/special/2016drive(_%2d)/\"},\n" +
                "               {\"url\": \"http://auto.163.com/guide/\", \"name\": \"导购\", \"more\": \"http://auto.163.com/special/2016buyers_guides(_%2d)/\"},\n" +
                "               {\"url\": \"http://auto.163.com/chezhu\", \"name\": \"用车\", \"more\": \"http://auto.163.com/special/2016chezhu(_%2d)/\"},\n" +
                "               {\"url\": \"http://auto.163.com/news/\", \"name\": \"行业\", \"more\": \"http://auto.163.com/special/2016news(_%2d)/\"}\n" +
                "            ]\n" +
                "        },\n" +
                "\t    {\n" +
                "            \"url\": \"http://ent.163.com/\",\n" +
                "            \"name\": \"娱乐新闻\",\n" +
                "            \"more\": \"http://ent.163.com/special/000380VU/newsdata_index(_%2d).js\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"url\": \"http://war.163.com/\",\n" +
                "            \"name\": \"军事新闻\",\n" +
                "            \"more\": \"http://temp.163.com/special/00804KVA/cm_war(_%2d).js\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"url\": \"http://news.163.com/world/\",\n" +
                "            \"name\": \"国际新闻\",\n" +
                "            \"more\": \"http://temp.163.com/special/00804KVA/cm_guoji(_%2d).js\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"url\": \"http://news.163.com/domestic/\",\n" +
                "            \"name\": \"国内新闻\",\n" +
                "            \"more\": \"http://temp.163.com/special/00804KVA/cm_guonei(_%2d).js\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"url\": \"http://edu.163.com/\",\n" +
                "            \"name\": \"教育\",\n" +
                "            \"more\": \"http://edu.163.com/special/002987KB/newsdata_edu_hot(_%2d).js\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"url\": \"http://travel.163.com/\",\n" +
                "            \"name\": \"旅游\",\n" +
                "            \"more\": \"http://travel.163.com/special/00067VEJ/newsdatas_travel(_%2d).js\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"url\": \"http://digi.163.com/\",\n" +
                "            \"name\": \"数码\",\n" +
                "            \"more\": \"http://digi.163.com/special/index_datalist(_%2d)/\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"url\": \"http://data.163.com/\",\n" +
                "            \"name\": \"数读\",\n" +
                "            \"more\": \"http://data.163.com/\"\n" +
                "        }\n" +
                "    ]\n" +
                "}", NewsModule.class);
        System.out.println(module);
        printLeaf(module);
    }

    private void printLeaf(NewsModule newsModule){
        if(CollectionUtils.isEmpty(newsModule.getNodes()) ){
            if(!newsModule.getIgnore()) {
                String x = NewsPageUrlGenerator.generatorPageUrl(newsModule.getMore(), 1);
                try {
//                    System.out.println(x + " " + PageUtil.getWebPage(new HttpGet(x), "GBK").getHtml().length());
                    System.out.println(x +" / "+newsModule.isOnePageModule());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else{
            for(NewsModule newsModule2 : newsModule.getNodes()){
                printLeaf(newsModule2);
            }
        }
    }
}
