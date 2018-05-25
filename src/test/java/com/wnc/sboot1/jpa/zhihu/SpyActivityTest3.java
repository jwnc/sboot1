
package com.wnc.sboot1.jpa.zhihu;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.wnc.basic.BasicDateUtil;
import com.wnc.sboot1.spy.service.UserVService;
import com.wnc.sboot1.spy.zhihu.ActivityLastTaskSpy;
import com.wnc.sboot1.spy.zhihu.active.UserV;

@RunWith( SpringRunner.class )
@SpringBootTest
public class SpyActivityTest3
{
    @Autowired
    private UserVService userVService;
    @Autowired
    private ActivityLastTaskSpy activityLastTaskSpy;

    // @Test
    public void a() throws Exception
    {
        List<UserV> lastTaskRetryUserVList = userVService
                .getLastTaskRetryUserVList( BasicDateUtil.getDateTimeFromString(
                        "2018-05-25 16:54:00", "yyyy-MM-dd HH:mm:ss" ) );
        System.out.println( lastTaskRetryUserVList.size() );
    }

    @Test
    public void b() throws Exception
    {
        activityLastTaskSpy.spy();
    }

}
