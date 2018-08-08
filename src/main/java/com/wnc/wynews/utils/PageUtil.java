
package com.wnc.wynews.utils;

import com.crawl.core.util.HttpClientUtil;
import com.crawl.spider.entity.Page;
import com.wnc.string.PatternUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class PageUtil {
    private static Set set = new HashSet<String>();

    public static Page getWebPage(HttpRequestBase request, String charset)
            throws IOException {
        CloseableHttpResponse response = HttpClientUtil.getResponse(request);
        Page page = new Page();
        page.setStatusCode(response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        String string = EntityUtils.toString(entity, charset);
        page.setHtml(string);
        string = null;
        entity = null;
        page.setUrl(request.getURI().toString());
        return page;
    }

    /**
     * @Description 获取评论code
     * @Date 2018/7/26 11:41
     * @Param url
     * @Return java.lang.String
     */
    public static String getCmtCode(String url) {
        String code = PatternUtil.getLastPatternGroup(url, ".+/(.*?)\\.html");
        if (StringUtils.isNotBlank(code) && code.length() > 10 && set.add(code)) {
            return code;
        }
        return "";
    }

}
