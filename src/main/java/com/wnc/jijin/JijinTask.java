
package com.wnc.jijin;

import com.wnc.basic.BasicDateUtil;
import com.wnc.basic.BasicFileUtil;
import com.wnc.jijin.CurrentValueData;
import com.wnc.jijin.JijinSpider;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class JijinTask
{
    //天弘创业板C, 可以把各个基金的code和name做成枚举
    String[] codes = {"001593","001630","000961","002987","004744","004069","502010","000369","110022","161725"};

    public JijinTask(){
        a();
    }

    public void a()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(5 * 60 * 1000L);
                        if(isWeekday() && isTradeTime()){
                            for (String code : codes) {
                                JijinSpider jijinSpider = new JijinSpider(code);
                                jijinSpider.downloadPic();
                                CurrentValueData currentValueData = jijinSpider.getCurrentValueData();
                                String folder = jijinSpider.getFolder();
                                BasicFileUtil.makeDirectory(folder);
                                BasicFileUtil.writeFileString(folder + "jijin-data-" + code + ".txt", code + " / "
                                        + currentValueData.getDatetime() + " / " + currentValueData.getGusuan() + "\r\n", null, true);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            private boolean isTradeTime() {
                String time = BasicDateUtil.getCurrentTimeString();
                return isAmTime(time)
                        || isPmTime(time);
            }

            private boolean isPmTime(String time) {
                return time.compareTo("13:00:00") >=0 && time.compareTo("15:10:00") <=0;
            }

            private boolean isAmTime(String time) {
                return time.compareTo("09:30:00") >= 0 && time.compareTo("11:40:00") <= 0;
            }

            private boolean isWeekday() {
                return BasicDateUtil.getCurrentWeekDay() >= 1 && BasicDateUtil.getCurrentWeekDay() <= 5;
            }
        }).start();

    }

}
