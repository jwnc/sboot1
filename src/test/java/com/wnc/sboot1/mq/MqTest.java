
package com.wnc.sboot1.mq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.wnc.basic.BasicDateUtil;
import com.wnc.sboot1.cluster.mq.zb8.Zb8HistoryDateSender;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MqTest {

//	@Autowired
//	Zb8HistoryDateSender zb8HistoryDateSender;
//
//	// @Test
//	public void a() {
//		String day = "2015-12-16";
//		while (!day.equals("2015-12-31")) {
//			day = BasicDateUtil.getDateBeforeDayDateString(day.replaceAll("-", ""), -1);
//			day = day.substring(0, 4) + "-" + day.substring(4, 6) + "-" + day.substring(6);
//			zb8HistoryDateSender.send(day);
//		}
//	}
//
//	@Test
//	public void b() {
//		zb8HistoryDateSender.send("2015-06-17");
//		zb8HistoryDateSender.send("2015-12-01");
//		zb8HistoryDateSender.send("2016-03-08");
//	}
}
