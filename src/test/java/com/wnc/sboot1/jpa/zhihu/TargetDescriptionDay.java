
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
        // zhihuActivityService.aggreYesterday();
        // zhihuActivityService.aggre("2018-05-07", "2018-05-08", 1);
        // zhihuActivityService.aggre("2018-05-06", "2018-05-07", 1);
        // zhihuActivityService.aggre("2018-05-05", "2018-05-06", 1);
        // zhihuActivityService.aggre("2018-05-04", "2018-05-05", 1);
        // zhihuActivityService.aggre("2018-05-03", "2018-05-04", 1);

        // zhihuActivityService.aggre("2018-05-02", "2018-05-03", 1);
        // zhihuActivityService.aggre("2018-05-01", "2018-05-02", 1);

        // zhihuActivityService.aggreLastWeek();
        // zhihuActivityService.aggreMonth();
        zhihuActivityService.aggreYear();
    }
}
