
package com.wnc.sboot1.util;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.wnc.sboot1.cluster.cron.ProxyTask;

@RunWith( SpringRunner.class )
@SpringBootTest
public class ProxyUtilTest
{
    @Autowired
    private ProxyTask proxyTask;

    @Test
    public void a() throws IOException
    {
        // ProxyUtil.get61Proxies();
        proxyTask.importAndCheckTask();
    }
}
