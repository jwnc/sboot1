
package com.wnc.sboot1.spy.task;

import com.wnc.basic.BasicFileUtil;
import com.wnc.jijin.CurrentValueData;
import com.wnc.jijin.JijinSpider;
import com.wnc.sboot1.spy.util.SpiderUtils;
import com.wnc.sboot1.spy.zuqiu.FunnyCommetSpy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.persistence.Basic;
import java.util.Calendar;

@Component
public class JijinTask
{
    private static volatile boolean flag = false;
    //天弘创业板C, 可以把各个基金的code和name做成枚举
    String[] codes = {"001593"};
    @Scheduled( cron = "${cronJob.fork_jijin_data}" )
    public void a()
    {
        try
        {
            if ( flag )
            {
                return;
            }
            flag = true;
            for(String code : codes){
                JijinSpider jijinSpider = new JijinSpider(code);
                jijinSpider.downloadPic();
                CurrentValueData currentValueData = jijinSpider.getCurrentValueData();
                BasicFileUtil.writeFileString("jijin-data"+code+".txt",code +" / "+currentValueData.getDatetime()+" / "+currentValueData.getGusuan()+"\r\n", null, true);
            }
        } catch ( Exception e )
        {
            e.printStackTrace();
        } finally
        {
            flag = false;
        }
    }

}
