package com.wnc.sboot1.cluster.util;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;

import com.crawl.core.util.HttpClientUtil;
import com.crawl.spider.entity.Page;

public class PageUtil {
	public static Page getWebPage(HttpRequestBase request, String charset) throws IOException {
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
}
