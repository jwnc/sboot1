
package com.wnc.sboot1.readlog;

import com.wnc.itbooktool.dao.DictionaryDao;
import com.wnc.itbooktool.word.DicWord;
import com.wnc.sboot1.itbook.service.BookKpiService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookKpiTest {
	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private BookKpiService booKpiService;

    @Test
    public void lineWeek() {
    	booKpiService.lineWeek();
    	Query createNativeQuery = entityManager.createNativeQuery( "select x.cn_name name, x.day day,"
    			+ "ifnull(t.cnt,0) cnt from (select * from device join (select day from daylist "
    			+ "where day >= '2019-04-23' and day <= '2019-04-24') d ) x left join "
    			+ "(select device, substr(log_time,0,11) day,count(*) cnt from itbook_log  "
    			+ "group by device,substr(log_time,0,11) )t on x.name=t.device and x.day = t.day order by x.day asc" );
    	Map map = new HashMap<>();
        List resultList = createNativeQuery.getResultList();
        String[] fieldArr = {"NAME", "DAY", "CNT"};
        int row = 1;
		for (Object obj : resultList) {
			Object[] arr = (Object[])obj;
			Map<String, String> fieldMap = new HashMap<String, String>();
			for (int i = 0; i < fieldArr.length; i++) {
				fieldMap.put(fieldArr[i], DictionaryDao.getArrStr(arr, i) );
			}
            map.put( row, fieldMap );
            row++;
		}
		System.out.println(map);
    }
}
