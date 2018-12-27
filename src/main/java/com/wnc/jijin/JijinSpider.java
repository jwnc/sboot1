package com.wnc.jijin;

import com.crawl.core.util.HttpClientUtil;
import com.wnc.basic.BasicDateUtil;
import com.wnc.basic.BasicFileUtil;
import com.wnc.basic.BasicNumberUtil;
import com.wnc.string.PatternUtil;
import com.wnc.tools.UrlPicDownloader;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import translate.util.JsoupHelper;

import java.util.ArrayList;
import java.util.List;

public class JijinSpider {
    private String jjCode;
    private Document doc;
    private static final String folder = "C:\\data\\spider\\jijin\\";

    public JijinSpider(String jjCode) throws Exception {
        this.jjCode = jjCode;
    }

    public String getFolder(){
        return folder+BasicDateUtil.getCurrentDateString()+"(周"+BasicDateUtil.getCurrentWeekDay()+")\\";
    }

    public void downloadPic() throws Exception {
        BasicFileUtil.makeDirectory(getFolder());
        UrlPicDownloader.download("http://j4.dfcfw.com/charts/pic6/" + this.jjCode + ".png",  getFolder()+jjCode + ".jpg");
    }

    /**
     * @Description 获取实时的估值数据, 建议做成定时任务
     * @Date 23:10 2018/12/5
     * @Param []
     * @return void
    */
    public CurrentValueData getCurrentValueData() throws Exception {
        //直接调接口
        String webPage = HttpClientUtil.getWebPage("http://fundgz.1234567.com.cn/js/"+this.jjCode+".js?rt="+System.currentTimeMillis());
        String gs = PatternUtil.getFirstPatternGroup(webPage, "\"gszzl\":\"([+\\-.0-9]+)\"");
        String datetime = PatternUtil.getLastPatternGroup(webPage, "\\d{2}:\\d{2}");
        return new CurrentValueData(BasicNumberUtil.getDouble(gs), datetime);
    }

    /**
     * @return java.util.List<com.wnc.jijin.ValueData>
     * @Description 获取近期增长率, 无法获取每日估值, 需要在三点后获取当日最后的估值
     * @Date 22:27 2018/12/5
     * @Param []
     */
    public List<ValueData> getLatelyValueData() throws Exception {
        List<ValueData> list = new ArrayList<ValueData>();
        doc = JsoupHelper.getDocumentResult("http://fund.eastmoney.com/" + jjCode + ".html");
        Elements select = doc.select("#Li1 > div.poptableWrap.singleStyleHeight01 > table tbody tr:gt(0)");
        for (Element element : select) {
            Elements tds = element.select("td");
            System.out.println(tds.get(0).text() + " / " + tds.get(3).text());
            list.add(new ValueData(tds.get(0).text(), BasicNumberUtil.getDouble(tds.get(3).text().replace("%", ""))));
        }
        return list;
    }
}
