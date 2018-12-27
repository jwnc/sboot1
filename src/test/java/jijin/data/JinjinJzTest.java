package jijin.data;

import com.crawl.core.util.HttpClientUtil;
import com.crawl.spider.entity.Page;
import com.wnc.basic.BasicDateUtil;
import com.wnc.sboot1.cluster.util.PageUtil;
import com.wnc.string.PatternUtil;
import org.apache.http.client.methods.HttpGet;
import org.junit.Test;

import java.io.IOException;
import java.util.Calendar;

/**
 * 基金净值测试
 * @author nengcai.wang
 * @version: 1.0
 * @since 2018/12/7 11:21
*/
public class JinjinJzTest {
    @Test
    public void a() throws IOException {
        String webPage = HttpClientUtil.getWebPage("http://fundgz.1234567.com.cn/js/001593.js?rt=1544147844887");
        System.out.println(webPage);
        String gs = PatternUtil.getFirstPatternGroup(webPage, "\"gszzl\":\"([+\\-.0-9]+)\"");
        String time = PatternUtil.getLastPatternGroup(webPage, "\\d{2}:\\d{2}");
        System.out.println(gs);
        System.out.println(time);
    }

    @Test
    public void b(){
        System.out.println(BasicDateUtil.getCurrentWeekDay());
        System.out.println(Calendar.getInstance().get(7) - 1);
    }

    @Test
    public void c() throws IOException {
        HttpGet httpGet = new HttpGet( "http://www.89ip.cn/tqdl.html?num=30&address=&kill_address=&port=&kill_port=&isp=" );
        Page webPage = PageUtil.getWebPage( httpGet, "GBK" );
        String content = webPage.getHtml();
        System.out.println(content);
    }
}
