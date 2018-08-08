
package com.wnc.wynews.utils;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.methods.HttpGet;

import com.crawl.proxy.ProxyPool;
import com.crawl.proxy.entity.Proxy;
import com.crawl.spider.entity.Page;
import com.wnc.basic.BasicNumberUtil;
import com.wnc.string.PatternUtil;

public class ProxyUtil {
    public void initProxyPool() throws IOException, InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (ProxyPool.proxyQueue.size() < 200) {
                            getProxy();
                        }
                        Thread.sleep(60 * 1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    private void getProxy() throws IOException {
        HttpGet httpGet = new HttpGet(
                "http://118.126.116.16:8080/sboot1/proxy/fatest200");
        Page webPage = PageUtil.getWebPage(httpGet, "UTF-8");
        String content = webPage.getHtml();
        List<String> get61Proxies = PatternUtil.getAllPatternGroup(content,
                "\\d+.\\d+.\\d+.\\d+:\\d+");
        for (String string : get61Proxies) {

            String ip = PatternUtil.getFirstPattern(string,
                    "\\d+.\\d+.\\d+.\\d+");
            int port = BasicNumberUtil
                    .getNumber(PatternUtil.getLastPattern(string, "\\d+"));
            System.out.println(ip+" / "+port);
            ProxyPool.proxyQueue.add(new Proxy(ip, port, 1000));
        }
        System.out.println(ProxyPool.proxyQueue.size());
    }

}
