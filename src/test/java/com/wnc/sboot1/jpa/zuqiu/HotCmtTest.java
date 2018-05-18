
package com.wnc.sboot1.jpa.zuqiu;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.wnc.sboot1.spy.util.SpiderUtils;
import com.wnc.sboot1.spy.zuqiu.FunnyCommetSpy;

@RunWith( SpringRunner.class )
@SpringBootTest
public class HotCmtTest
{
    @Autowired
    private FunnyCommetSpy funnyCommetSpy;

    @Test
    public void a() throws Exception
    {
        funnyCommetSpy.setSpyDay( SpiderUtils.getYesterDayStr() ).spy();
        funnyCommetSpy.setSpyDay( SpiderUtils.getDayWithLine() ).spy();
    }

}
