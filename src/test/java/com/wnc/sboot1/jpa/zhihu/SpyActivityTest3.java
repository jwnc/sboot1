
package com.wnc.sboot1.jpa.zhihu;


import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.wnc.sboot1.spy.service.UserVService;
import com.wnc.sboot1.spy.zhihu.active.UserV;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SpyActivityTest3
{
    @Autowired
    private UserVService userVService;

    @Test
    public void a()
        throws Exception
    {
        List<UserV> initUserVList = userVService.getInitUserVList();
        System.out.println(initUserVList);
    }

}
