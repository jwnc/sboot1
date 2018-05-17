
package com.wnc.sboot1.jpa.zhihu;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.wnc.sboot1.spy.zhihu.ActivityRetrySpy;

@RunWith( SpringRunner.class )
@SpringBootTest
public class SpyActivityTest
{
    @Autowired
    private ActivityRetrySpy activityRetrySpy;

    @Test
    public void a() throws Exception
    {
        activityRetrySpy.spy();
    }

}
