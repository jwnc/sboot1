
package com.wnc.sboot1.jpa.zhihu;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSONArray;
import com.wnc.sboot1.spy.service.ZhihuActivityService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SqlTest {
	private static Logger logger = Logger.getLogger(SqlTest.class);
	@Autowired
	private ZhihuActivityService zhihuActivityService;

	@Test
	public void a() {
		System.out.println(JSONArray.toJSONString(zhihuActivityService.getAggreData("2018-05-08")));
	}

}
