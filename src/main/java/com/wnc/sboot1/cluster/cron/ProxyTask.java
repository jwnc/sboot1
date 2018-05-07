
package com.wnc.sboot1.cluster.cron;

import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wnc.sboot1.cluster.service.ProxyHashService;
import com.wnc.sboot1.cluster.service.ProxyListService;
import com.wnc.sboot1.cluster.util.ProxyUtil;

@Component
public class ProxyTask {
	@Autowired
	private ProxyHashService proxyHashService;
	private Logger logger = Logger.getLogger(ProxyTask.class);

	@Scheduled(cron = "${cronJob.import_proxy}")
	private void importAndCheckTask() {
		sleep();
		if (ProxyUtil.taskFlag1) {
			logger.info("importAndCheckTask 正在运行, 本线程退出");
			return;
		}
		ProxyUtil.taskFlag1 = true;
		logger.info("代理任务启动:importTask ");
		try {
			if (!proxyHashService.isEnough()) {
				int importProxies = proxyHashService.importProxies();
				logger.info("导入新代理数目: " + importProxies);
			} else {
				logger.info("有效代理够用,无需导入");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				proxyHashService.checkAll();
			} catch (Exception e) {
				logger.error("代理导入异常.", e );
				e.printStackTrace();
			} finally {
				proxyHashService.saveFatestProxies();
				ProxyUtil.taskFlag1 = false;
			}
		}
	}

	private void sleep() {
		try {
			Thread.sleep(new Random().nextInt(10000));
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	@Scheduled(cron = "${cronJob.remove_proxy}")
	private void removeDisabled() {
		sleep();
		if (ProxyUtil.taskFlag2) {
			logger.info("removeDisabled 正在运行, 本线程退出");
			return;
		}
		ProxyUtil.taskFlag2 = true;
		logger.info("代理任务启动:removeDisabled ");
		try {
			proxyHashService.removeDisableProxy();
		} catch (Exception e) {
			logger.error("失效代理删除异常.",e );
			e.printStackTrace();
		}finally {
			ProxyUtil.taskFlag2 = false;
		}
	}

}