package com.wnc.sboot1.itbook.task;

import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.wnc.basic.BasicDateUtil;
import com.wnc.sboot1.itbook.service.BookKpiService;
import com.wnc.sboot1.service.impl.MailService;

@Component
public class MailSendTask {

	private static final Logger LOGGER = Logger.getLogger(MailSendTask.class);
	@Autowired
	private MailService mailService;
	@Autowired
	TemplateEngine templateEngine;
	@Autowired
	BookKpiService bookKpiService;

	@Value("${itbook.emailto}")
	private String emailTo;

	@Value("${spring.profiles.active}")
	private String mode;
	@Value("${cronJob.mail_retry_IntervalMinutes}")
	private int retryIntervalMinutes;
	@Value("${cronJob.mail_retry_times}")
	private int retryTimes;

	private static volatile boolean flag = false;

	@Scheduled(cron = "${cronJob.mail_week}")
	public void sendTemplateMail() {
		String today = getToday();
		send("七日阅读统计", BasicDateUtil.getDateBeforeDayDateString(today, 6) + "-" + today + ")",
				bookKpiService.lineWeek4Mail());
	}

	// @Scheduled(cron = "${cronJob.mail_yesterday}")
	public void sendLastDayTemplateMail() {
		send("昨日阅读统计", BasicDateUtil.getDateBeforeDayDateString(getToday(), 1),
				bookKpiService.lineWeek4Mail().subList(5, 6));
	}

	// @Scheduled(cron = "${cronJob.mail_today}")
	public void sendToDayTemplateMail() {
		send("今日阅读统计", getToday(), bookKpiService.lineWeek4Mail().subList(6, 7));
	}

	private void send(String summary, String dateStr, List list) {
		sleep();
		if (flag) {
			return;
		}
		flag = true;
		LOGGER.info("【" + summary + "】开始自动发送邮件给" + emailTo);
		try {
			// 创建邮件正文
			Context context = new Context();
			context.setVariable("summary", "【" + summary + "】");
			context.setVariable("dataList", list);
			String emailContent = templateEngine.process("emailTemplate", context);
			int i = 0;
			while (i < retryTimes) {
				i++;
				boolean sendHtmlMail = mailService.sendHtmlMail(emailTo, summary + "(" + dateStr + ")", emailContent);
				if (sendHtmlMail) {
					break;
				}
				LOGGER.info("第" + i + "次发送失败, 过" + retryIntervalMinutes + "分钟重试");
				Thread.sleep(1000 * 60 * retryIntervalMinutes);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			flag = false;
		}
	}

	private String getToday() {
		return BasicDateUtil.getCurrentDateString();
	}

	private void sleep() {
		try {
			Thread.sleep(new Random().nextInt(10000));
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}