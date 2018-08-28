
package com.wnc;

import com.wnc.sboot1.spy.zuqiu.TaskCompleteLog;
import com.wnc.sboot1.spy.zuqiu.rep.TaskCompleteLogRepository;
import com.wnc.wynews.repo.TestLog;
import com.wnc.wynews.repo.LogRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JpaTest {
    @Autowired
    private LogRepository logRepository;
    @Autowired
    private TaskCompleteLogRepository taskCompleteLogRepository;

    @Test
    public void d() throws IOException, InterruptedException {
        TestLog taskCompleteLog = new TestLog();
        taskCompleteLog.setUrl("xxx");
        logRepository.save(taskCompleteLog);
    }

}
