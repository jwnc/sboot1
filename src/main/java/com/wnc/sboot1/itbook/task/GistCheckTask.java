
package com.wnc.sboot1.itbook.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wnc.sboot1.itbook.helper.GistHelper;

@Component
public class GistCheckTask
{
    @Scheduled( cron = "0 0 0-23/8 * * ?" )
    private void process()
    {
        try
        {
            new GistHelper().checkGist();
        } catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
}