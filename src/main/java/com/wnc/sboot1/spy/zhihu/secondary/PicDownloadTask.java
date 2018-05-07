
package com.wnc.sboot1.spy.zhihu.secondary;

import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;

import com.crawl.core.util.HttpClientUtil;
import com.crawl.proxy.ProxyPool;
import com.crawl.proxy.entity.Direct;
import com.crawl.proxy.entity.Proxy;
import com.crawl.spider.SpiderHttpClient;
import com.wnc.basic.BasicFileUtil;

public class PicDownloadTask implements Runnable {
	private String url;
	private String savePath;

	public PicDownloadTask(String url, String savePath) {
		this.url = url;
		this.savePath = savePath;
	}

	@Override
	public void run() {
		Proxy currentProxy = null;
		HttpRequestBase tempRequest = null;
		boolean okProxy = true;
		boolean exOccur = false;
		try {
			currentProxy = ProxyPool.proxyQueue.take();
			tempRequest = new HttpGet(url);
			if (!(currentProxy instanceof Direct)) {
				HttpHost proxy = new HttpHost(currentProxy.getIp(), currentProxy.getPort());
				tempRequest.setConfig(HttpClientUtil.getRequestConfigBuilder().setProxy(proxy).build());
			}
			FileDownloader.downloadFile(tempRequest, savePath);
		} catch (Exception e) {
			exOccur = true;
			BasicFileUtil.writeFileString(ParsePicsTask.ERR_LOG, savePath + "下载失败 " + e.toString() + "\r\n", null,
					true);
			BasicFileUtil.deleteFile(savePath);

			if (!(currentProxy instanceof Direct) && e.getMessage().contains(currentProxy.getProxyStr())) {
				okProxy = false;
			}
		} finally {
			if (tempRequest != null) {
				try {
					tempRequest.releaseConnection();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (okProxy)
				ProxyPool.proxyQueue.put(currentProxy);
			if (BasicFileUtil.getFileSize(savePath) == 0) {
				BasicFileUtil.deleteFile(savePath);
			}
			if (exOccur) {
				retry();
			}
		}
	}

	private void retry() {
		SpiderHttpClient.getInstance().getNetPageThreadPool().execute(new PicDownloadTask(url, savePath));
	}

}
