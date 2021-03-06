
package com.wnc.sboot1.readlog;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReadLogTest {
    @Autowired
    private ReadLogRepository readLogRepository;
    @Autowired
    private DictionaryRepository dictRepository;

    @Test
    public void testFindOne2() {
        System.out.println(dictRepository.findOne(98155L));
    }
    
    @Test
    public void testFindOne() {
        System.out.println(readLogRepository.findOne(2L));
    }
    
    @Test
    public void testInsert() {
    	ReadLog readLog = new ReadLog();
    	readLog.setContent("test-expiration");
    	readLog.setDictId(125423L);
    	readLog.setType(1);
        System.out.println(readLogRepository.save(readLog));
    }

}
