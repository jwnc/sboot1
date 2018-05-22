
package com.wnc.sboot1.jpa.zhihu;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.wnc.sboot1.spy.zhihu.ActivityContinueSpy;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SpyActivityTest4
{
    @Autowired
    private ActivityContinueSpy activityContinueSpy;

    @Test
    public void a()
        throws Exception
    {
        activityContinueSpy.spy();
    }

}
