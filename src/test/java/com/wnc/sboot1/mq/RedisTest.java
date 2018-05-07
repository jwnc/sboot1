
package com.wnc.sboot1.mq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.wnc.sboot1.cluster.service.ProxyHashService;

@RunWith( SpringRunner.class )
@SpringBootTest
public class RedisTest
{

    @Autowired
    ProxyHashService proxyHashService;

    @Test
    public void a()
    {
        proxyHashService.saveFatestProxies();
    }
}
