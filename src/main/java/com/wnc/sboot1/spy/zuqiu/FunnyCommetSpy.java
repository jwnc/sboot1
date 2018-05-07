
package com.wnc.sboot1.spy.zuqiu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.crawl.spider.SpiderHttpClient;
import com.crawl.spider.task.AbstractPageTask;
import com.wnc.basic.BasicFileUtil;

@Component
public class FunnyCommetSpy {
	private volatile int cmtTopicCount = 0;
	private int taskCount = 0;
	private long startTime = 0L;

	private volatile int retryFaildCount = 0;
	private volatile int status404Count = 0;

	@Value("${spy.cmt_fork_rate}")
	private double rate;
	private Logger logger = Logger.getLogger(FunnyCommetSpy.class);

	private ThreadPoolExecutor netPageThreadPool = SpiderHttpClient.getInstance().getNetPageThreadPool();

	private Map<Zb8News, List<HotComment>> map;
	private List<Zb8News> solvedList;

	/**
	 * 内部爬虫任务接口 - 完成任务之后的回调
	 * 
	 * @param type
	 * @param msg
	 * @param news
	 */
	public synchronized void completeCallback(int type, String msg, Zb8News news) {
		cmtTopicCount++;
		switch (type) {
		case AbstractPageTask.COMPLETE_STATUS_SUCCESS:
			this.solvedList.add(news);
			break;
		case AbstractPageTask.COMPLETE_STATUS_FAIL_RETRY_OUT:
			retryFaildCount++;
			break;
		case AbstractPageTask.COMPLETE_STATUS_FAIL_404:
			status404Count++;
			break;
		}

		System.out.println("当前完成任务数目:" + cmtTopicCount);
		BasicFileUtil.writeFileString("complete.txt", msg + "\r\n", null, true);
	}

	/**
	 * 任务全部结束或者时间超时, 最多rate秒一个
	 * 
	 * @param size
	 * @param startTime
	 * @return
	 */
	public boolean isOverTask() {
		if (rate <= 0) {
			rate = 1;
		}
		return cmtTopicCount >= taskCount || getSpyDuration() > Math.max(rate * taskCount * 1000, 300 * 1000);
	}

	/**
	 * 内部爬虫任务接口
	 * 
	 * @return
	 */
	public Map<Zb8News, List<HotComment>> getMap() {
		return map;
	}

	/**
	 * 外部任务借口
	 * 
	 * @param day
	 * @return
	 * @throws Exception
	 */
	public Map<Zb8News, List<HotComment>> getFunnyAll(String day) throws Exception {
		reset();

		Map<Zb8News, List<HotComment>> funnyNBA = getFunnyNBA(day);
		funnyNBA.putAll(getFunnyZuqiu(day));

		while (true) {
			logger.info(getMonitorLog());

			if (isOverTask()) {
				break;
			}
			Thread.sleep(10000);
		}
		logger.info("任务结束用时:" + getSpyDuration() + " 抓取新闻总数:" + map.size());

		return funnyNBA;
	}

	/**
	 * 外部任务借口, 返回去做数据库或其他操作
	 * 
	 * @return
	 */
	public List<Zb8News> getSolvedList() {
		return solvedList;
	}

	/**
	 * 拼接当前监控日志
	 * 
	 * @return
	 */
	private String getMonitorLog() {
		StringBuilder accum = new StringBuilder("");
		accum.append("当前进度:" + cmtTopicCount + "/" + taskCount);
		accum.append(" 重试失败:" + retryFaildCount);
		accum.append(" 重试中:" + (AbstractPageTask.retryMap.get(FunnyCmtTask.class).size() - retryFaildCount));
		accum.append(" 404请求数:" + status404Count);
		accum.append(" 耗时秒数:" + getSpyDuration() / 1000);
		return accum.toString();
	}

	private Map<Zb8News, List<HotComment>> getFunnyNBA(String day) throws Exception {
		return getFunnyComments(Zb8Const.NBA, day);
	}

	private Map<Zb8News, List<HotComment>> getFunnyZuqiu(String day) throws Exception {
		return getFunnyComments(Zb8Const.ZUQIU, day);
	}

	/**
	 * @param day
	 *            格式2018-04-16
	 * @return
	 * @throws IOException
	 */
	private Map<Zb8News, List<HotComment>> getFunnyComments(String catelog, String day) throws Exception {
		Set<String> set = new HashSet<String>();
		List<Zb8News> list = new Zb8NewsService().getNews(catelog, day);
		for (Zb8News news : list) {
			if (set.add(news.getPinglun())) {
				taskCount++;
				netPageThreadPool.execute(new FunnyCmtTask(this, news));
			}
		}

		return getMap();
	}

	/**
	 * 获取spy持续进行的时间
	 * 
	 * @param startTime
	 * @return
	 */
	private long getSpyDuration() {
		return System.currentTimeMillis() - startTime;
	}

	/**
	 * 重置各个类变量
	 */
	private void reset() {
		map = new HashMap<Zb8News, List<HotComment>>();
		solvedList = Collections.synchronizedList(new ArrayList<Zb8News>());
		startTime = System.currentTimeMillis();
		cmtTopicCount = 0;
		taskCount = 0;
		retryFaildCount = 0;
		status404Count = 0;
		if (AbstractPageTask.retryMap.get(FunnyCmtTask.class) != null) {
			AbstractPageTask.retryMap.get(FunnyCmtTask.class).clear();
		}
		AbstractPageTask.retryMap.put(FunnyCmtTask.class, new ConcurrentHashMap<String, Integer>());
	}

}
