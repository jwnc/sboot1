
package com.wnc.sboot1.jpa.zhihu;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.wnc.sboot1.spy.zhihu.active.aggre.TargetAggreInfo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SqlTest {
	private static Logger logger = Logger.getLogger(SqlTest.class);
	@PersistenceContext
	private EntityManager entityManager;

	@Test
	public void a() {
		String sql = "select act.cnt,tar.id tid,tar.info,tar.type,tar.url from (SELECT target_id, count(*) cnt FROM zh_activity WHERE FROM_UNIXTIME(created_time) >= '2018-05-01 00:00;00' and FROM_UNIXTIME(created_time) < '2018-05-08 00:00:00' group by target_id having count(*) > 20) act, zh_target tar where act.target_id=tar.id";
		Query createNativeQuery = entityManager.createNativeQuery(sql, TargetAggreInfo.class);
		System.out.println("查询数据返回map" + createNativeQuery.getResultList());
	}

}
