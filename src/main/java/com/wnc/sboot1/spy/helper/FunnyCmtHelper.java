
package com.wnc.sboot1.spy.helper;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wnc.sboot1.spy.rep.HotCommentRepository;
import com.wnc.sboot1.spy.rep.TaskCompleteLogRepository;
import com.wnc.sboot1.spy.rep.Zb8NewsRepository;
import com.wnc.sboot1.spy.util.ProxyProcess;
import com.wnc.sboot1.spy.util.SpiderUtils;
import com.wnc.sboot1.spy.zuqiu.FunnyCommetSpy;
import com.wnc.sboot1.spy.zuqiu.HotComment;
import com.wnc.sboot1.spy.zuqiu.TaskCompleteLog;
import com.wnc.sboot1.spy.zuqiu.Zb8News;

@Component
public class FunnyCmtHelper {
	@Autowired
	private Zb8NewsRepository zb8NewsRepository;
	@Autowired
	private HotCommentRepository hotCommentRepository;
	@Autowired
	private TaskCompleteLogRepository taskCompleteLogRepository;
	@Autowired
	private FunnyCommetSpy funnyCommetSpy;

	public void forkByDay(String day) throws Exception {
		ProxyProcess.getInstance().init();
		Map<Zb8News, List<HotComment>> funnyComments = funnyCommetSpy.getFunnyAll(day);

		Iterator<Entry<Zb8News, List<HotComment>>> iterator = funnyComments.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Zb8News, List<HotComment>> entry = iterator.next();
			singleNews(entry);
		}
		saveCompleteLog();
	}

	public void forkToday() throws Exception {
		forkByDay(SpiderUtils.getDayWithLine());
	}

	public void forkYesterday() throws Exception {
		forkByDay(SpiderUtils.getYesterDayStr());
	}

	// @Transactional( propagation = Propagation.REQUIRES_NEW )
	private void singleNews(Map.Entry<Zb8News, List<HotComment>> entry) {
		try {
			zb8NewsRepository.save(entry.getKey());
			for (HotComment hotComment : entry.getValue()) {
				hotCommentRepository.save(hotComment);
			}
		} catch (Exception e) {
			System.err.println(entry.getKey().getTitle());
			e.printStackTrace();
		}
	}

	private void saveCompleteLog() {
		try {
			List<Zb8News> solvedList = funnyCommetSpy.getSolvedList();
			TaskCompleteLog taskCompleteLog;
			for (Zb8News zb8News : solvedList) {
				taskCompleteLog = new TaskCompleteLog();
				taskCompleteLog.setTitle(zb8News.getTitle());
				taskCompleteLog.setUrl("https://news.zhibo8.cc" + zb8News.getUrl());
				taskCompleteLog.setSite("zb8");
				taskCompleteLog.setModule("news");
				taskCompleteLogRepository.save(taskCompleteLog);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
