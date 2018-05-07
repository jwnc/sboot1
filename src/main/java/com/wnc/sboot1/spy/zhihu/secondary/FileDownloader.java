
package com.wnc.sboot1.spy.zhihu.secondary;

import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

import com.crawl.core.util.HttpClientUtil;

public class FileDownloader {

	public static void downloadFile(HttpRequestBase requestBase, String savePath) throws Exception {

		CloseableHttpResponse response = null;
		FileOutputStream output = null;
		InputStream in = null;
		try {
			response = HttpClientUtil.getResponse(requestBase);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				output = new FileOutputStream(savePath);
				in = response.getEntity().getContent();
				int ch;
				while ((ch = in.read()) != -1) {
					output.write(ch);
				}
				output.close();
			} else {
				// 如果响应code错误, 则不能生成图片
				throw new RuntimeException(" Response Status:" + statusCode);
			}

		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (output != null)
						output.close();
				}
			}
			if (response != null) {
				response.close();
			}
		}

		System.out.println(requestBase.getURI() + "--文件成功下载至" + savePath);

	}
}
