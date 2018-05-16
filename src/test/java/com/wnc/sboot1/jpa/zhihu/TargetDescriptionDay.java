
package com.wnc.sboot1.jpa.zhihu;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.wnc.sboot1.spy.service.ZhihuActivityService;

/**
 * 按日期保存统计信息
 * 
 * @author wnc
 */
@RunWith( SpringRunner.class )
@SpringBootTest
public class TargetDescriptionDay
{
    @Autowired
    private ZhihuActivityService zhihuActivityService;

    @Test
    public void a()
    {

        zhihuActivityService.aggre( "2018-04-30", "2018-04-30", 1,
                ZhihuActivityService.FOLLOW_DAY_COUNT );
        for ( int i = 1; i < 16; i++ )
        {
            String s = i + "";
            if ( i < 10 )
            {
                s = "0" + s;
            }
            zhihuActivityService.aggre( "2018-05-" + s, "2018-05-" + s, 1,
                    ZhihuActivityService.FOLLOW_DAY_COUNT );
        }

        zhihuActivityService.aggre( "2018-04-30", "2018-05-06",
                ZhihuActivityService.AGGRE_WEEK_CODE,
                ZhihuActivityService.FOLLOW_WEEK_COUNT );
        zhihuActivityService.aggre( "2018-05-07", "2018-05-13",
                ZhihuActivityService.AGGRE_WEEK_CODE,
                ZhihuActivityService.FOLLOW_WEEK_COUNT );
        zhihuActivityService.aggre( "2018-05-14", "2018-05-20",
                ZhihuActivityService.AGGRE_WEEK_CODE,
                ZhihuActivityService.FOLLOW_WEEK_COUNT );

        // zhihuActivityService.aggreLastWeek();
        zhihuActivityService.aggreMonth();
        zhihuActivityService.aggreYear();
    }
}
