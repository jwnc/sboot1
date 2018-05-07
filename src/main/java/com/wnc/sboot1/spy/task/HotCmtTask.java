
package com.wnc.sboot1.spy.task;

import java.util.Calendar;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wnc.basic.BasicDateUtil;
import com.wnc.basic.BasicFileUtil;
import com.wnc.sboot1.spy.helper.FunnyCmtHelper;

@Component
public class HotCmtTask {
	@Autowired
	private FunnyCmtHelper funnyCmtService;
	private static volatile boolean flag = false;
	private static volatile boolean flag17 = false;

	@Value("${spy.cmt_fork_2017_lastday}")
	private String lastDay;
	@Autowired
	FunnyCmtHelper funnyCmtHelper;

	@Scheduled(cron = "${cronJob.fork_zb8_comment}")
	public void a() {
		sleep();
		if (flag) {
			return;
		}
		flag = true;
		try {
			funnyCmtService.forkToday();
			int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
			// 同时去找昨天的新闻
			if (hour < 10) {
				funnyCmtService.forkYesterday();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			flag = false;
		}
	}

	@Scheduled(cron = "${cronJob.fork_zb8_comment_2017}")
	public void b() {
		sleep();
		if (flag17) {
			return;
		}
		flag17 = true;
		try {
			while (!lastDay.equals("2018-05-04")) {
				lastDay = BasicDateUtil.getDateBeforeDayDateString(lastDay.replaceAll("-", ""), 1);
				lastDay = lastDay.substring(0, 4) + "-" + lastDay.substring(4, 6) + "-" + lastDay.substring(6);
				try {
					funnyCmtHelper.forkByDay(lastDay);
					BasicFileUtil.writeFileString("c:/spy.log",
							BasicDateUtil.getCurrentDateTimeString() + " : FunnyCmt Over-" + lastDay + "\r\n", null,
							true);

				} catch (Exception e) {
					e.printStackTrace();
					BasicFileUtil.writeFileString("spy.log",
							BasicDateUtil.getCurrentDateTimeString() + " : FunnyCmt-Err in " + lastDay + "\r\n", null,
							true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			flag17 = false;
		}
	}

	public static void main(String[] args) throws Exception {
		new HotCmtTask().a();
	}

	private void sleep() {
		try {
			Thread.sleep(new Random().nextInt(10000));
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}
