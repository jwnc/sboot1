
package com.wnc.sboot1.jpa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.wnc.basic.BasicDateUtil;
import com.wnc.basic.BasicFileUtil;
import com.wnc.sboot1.spy.helper.FunnyCmtHelper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpyFunnyCmt2016 {

	@Autowired
	FunnyCmtHelper funnyCmtHelper;

	// @Test
	public void backward() {
		String day = "2017-01-01";
		while (!day.equals("2016-01-01")) {
			day = BasicDateUtil.getDateBeforeDayDateString(day.replaceAll("-", ""), 1);
			day = day.substring(0, 4) + "-" + day.substring(4, 6) + "-" + day.substring(6);
			try {
				funnyCmtHelper.forkByDay(day);
				BasicFileUtil.writeFileString("spy.log",
						BasicDateUtil.getCurrentDateTimeString() + " : FunnyCmt Over-" + day + "\r\n", null, true);

			} catch (Exception e) {
				e.printStackTrace();
				BasicFileUtil.writeFileString("spy.log",
						BasicDateUtil.getCurrentDateTimeString() + " : FunnyCmt-Err in " + day + "\r\n", null, true);
			}
		}
	}

	// @Test
	public void one() throws Exception {
		String day = "2015-01-01";
		funnyCmtHelper.forkByDay(day);
		BasicFileUtil.writeFileString("spy.log",
				BasicDateUtil.getCurrentDateTimeString() + " : FunnyCmt Over-" + day + "\r\n", null, true);
	}

	@Test
	public void forward() {
		String day = "2015-05-09";
		while (!day.equals("2017-12-31")) {
			day = BasicDateUtil.getDateBeforeDayDateString(day.replaceAll("-", ""), -1);
			day = day.substring(0, 4) + "-" + day.substring(4, 6) + "-" + day.substring(6);
			try {
				funnyCmtHelper.forkByDay(day);
				BasicFileUtil.writeFileString("spy.log",
						BasicDateUtil.getCurrentDateTimeString() + " : FunnyCmt Over-" + day + "\r\n", null, true);

			} catch (Exception e) {
				e.printStackTrace();
				BasicFileUtil.writeFileString("spy.log",
						BasicDateUtil.getCurrentDateTimeString() + " : FunnyCmt-Err in " + day + "\r\n", null, true);
			}
		}
	}
}
