package com.wnc.sboot1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import com.wnc.itbooktool.dao.DictionaryDao;
import com.wnc.sboot1.readlog.Dictionary;

@Configuration
public class InstantiationTracingBeanPostProcessor implements ApplicationListener<ContextRefreshedEvent> {
	@Autowired
	private DictionaryDao dictionaryDao;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		dictionaryDao.initDictToMem();
	}

}
