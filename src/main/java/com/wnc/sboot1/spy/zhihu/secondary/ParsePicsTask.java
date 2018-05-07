
package com.wnc.sboot1.spy.zhihu.secondary;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawl.spider.SpiderHttpClient;
import com.wnc.basic.BasicFileUtil;
import com.wnc.string.PatternUtil;
import com.wnc.tools.FileOp;

public class ParsePicsTask {
	public static final String ERR_LOG = "D:\\个人工作\\spy\\zhihu\\topics\\表情图\\err.log";
	public static final String ALLPICS_LOG = "D:\\个人工作\\spy\\zhihu\\topics\\表情图\\all_pics.log";

	static String folder = "D:\\个人工作\\spy\\zhihu\\topics\\表情图\\";

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		// System.setProperty("log4j.logger.httpclient.wire.header", "INFO");
		// System.setProperty("log4j.logger.org.apache.commons.httpclient", "INFO");

		// initFolders();
		//
		// try {
		// ProxyProcess.getInstance().init();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// downLoadAll();
		filterUnDownload();
		// testValid();
		// testDebug();
	}

	private static void testDebug() {
		SpiderHttpClient.getInstance().getNetPageThreadPool().execute(new PicDownloadTask(
				"https://pic3.zhimg.com/50/7086c45d2bb892c713e4bfbda13b34e7_hd.jpg", "D:/debug-bqb.jpg"));
	}

	private static void filterUnDownload() {

		List<String> readFrom = FileOp.readFrom(ALLPICS_LOG);
		for (String string : readFrom) {
			JSONObject parseObject = JSONObject.parseObject(string);
			String picUrl = parseObject.getString("key");
			String filename = parseObject.getString("val");
			if (!BasicFileUtil.isExistFile(filename)) {
				System.err.println(picUrl);
				SpiderHttpClient.getInstance().getNetPageThreadPool().execute(new PicDownloadTask(picUrl, filename));
			}
		}
	}

	private static void downLoadAll() {
		List<String> readFrom = FileOp.readFrom(ALLPICS_LOG);
		for (String string : readFrom) {
			JSONObject parseObject = JSONObject.parseObject(string);
			String picUrl = parseObject.getString("key");
			String filename = parseObject.getString("val");
			SpiderHttpClient.getInstance().getNetPageThreadPool().execute(new PicDownloadTask(picUrl, filename));
		}
	}

	private static void initFolders() {
		Set<String> set = new HashSet<String>();
		for (File file : new File(folder).listFiles()) {
			if (!file.getName().endsWith(".json")) {
				continue;
			}
			JSONArray jsonArray = JSONObject.parseArray(StringUtils.join(FileOp.readFrom(file.getAbsolutePath())))
					.getJSONObject(0).getJSONArray("data");
			for (int i = 0; i < jsonArray.size(); i++) {

				String content = getContent(jsonArray.getJSONObject(i));
				List<String> allPatternGroup = PatternUtil.getAllPatternGroup(content, "src=\"(http.*?)\"");
				if (allPatternGroup.size() == 0) {
					System.err.println("Err 无图片:" + file.getAbsolutePath() + " " + i);
					BasicFileUtil.writeFileString(ERR_LOG, "Err 无图片:" + file.getAbsolutePath() + " " + i + "\r\n", null,
							true);
					continue;
				}
				String qtitle = getTitle(jsonArray.getJSONObject(i));
				String folderSon = folder + qtitle.replaceAll("[\\.\\|\\[\\]\"\\'\\<\\>\\*\\?\\:/\\\\ ]", "").trim();
				boolean makeDirectory = BasicFileUtil.makeDirectory(folderSon);
				if (!makeDirectory) {
					BasicFileUtil.writeFileString(ERR_LOG, qtitle + "无法创建文件夹\r\n", null, true);
					continue;
				}

				for (String picUrl : allPatternGroup) {
					if (picUrl.contains("equation?tex=")) {
						continue;
					}

					boolean add = set.add(picUrl);
					String filename = folderSon + "\\" + BasicFileUtil.getFileName(picUrl);
					if (add) {
						JSONObject json = new JSONObject();
						json.put("key", picUrl);
						json.put("val", filename);
						BasicFileUtil.writeFileString(ALLPICS_LOG, json.toJSONString() + "\r\n", null, true);
					}
				}
			}
		}
	}

	private static String getContent(JSONObject jsonObject) {
		String string = jsonObject.getJSONObject("target").getString("content");
		if (StringUtils.isBlank(string)) {
			System.err.println("Blank content:" + jsonObject.toJSONString());
		}
		return string;
	}

	private static String getTitle(JSONObject jsonObject) {
		JSONObject target = jsonObject.getJSONObject("target");
		String title = target.getString("title");
		if (StringUtils.isBlank(title)) {
			JSONObject tObject = target.getJSONObject("question");
			if (tObject == null) {
				tObject = target.getJSONObject("column");
				title = tObject.getString("title");
			} else {
				title = tObject.getString("title");
			}
			if (StringUtils.isBlank(title)) {
				title = "Others";
				System.err.println("Blank title:" + jsonObject.toJSONString());
			}
		}
		return title;
	}

	/**
	 * 检查是否为有效图片
	 */
	private static void testValid() {
		int cc = 0;
		String folder = "D:\\个人工作\\spy\\zhihu\\topics\\表情图\\";
		for (File file : new File(folder).listFiles()) {
			if (!file.getName().endsWith(".json") && !file.getName().endsWith(".log")) {
				for (File file2 : file.listFiles()) {
					boolean deletable = false;
					FileInputStream input = null;
					try {
						input = new FileInputStream(file2);
						BufferedImage sourceImg = ImageIO.read(input);
						if (sourceImg == null || sourceImg.getHeight() == 0) {
							System.out.println((++cc) + "  " + file2.getAbsolutePath() + " Height = 0");
							deletable = true;
						}
					} catch (Exception e) {
						System.out.println("Err:" + file2.getAbsolutePath());
						BasicFileUtil.writeFileString(ERR_LOG, file2.getAbsolutePath() + " / " + e.toString() + " \r\n",
								null, true);
						e.printStackTrace();
					} finally {
						// 先释放资源再删除
						if (input != null) {
							try {
								input.close();
								if (deletable)
									BasicFileUtil.deleteFile(file2.getAbsolutePath());
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}

		// try
		// {
		// Thread.sleep( 1000000 );
		// } catch ( InterruptedException e )
		// {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}
}
