package com.wnc.sboot1.cluster.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProxyListService {
	public static final int POOL_SIZE = 400;
	private final int BUFFER_SIZE = 100;
	public static final String redisListKey = "proxyList";

	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	/**
	 * 获取缓存,每次取BUFFER_SIZE=100条
	 * 
	 * @return
	 * @throws IOException
	 */
	public List<String> getFatestProxies() throws IOException {
		List<String> list = new ArrayList<String>();
		ListOperations<String, String> opsForList = stringRedisTemplate.opsForList();
		Long size = opsForList.size(redisListKey);
		int i = 0;
		while (i < size) {
			List<String> range = opsForList.range(redisListKey, i, i + BUFFER_SIZE);
			for (String string : range) {
				list.add(string);
			}
			i += BUFFER_SIZE;
		}
		return list;
	}

	
}
