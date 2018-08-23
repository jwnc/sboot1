
package com.wnc.sboot1.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.wnc.basic.BasicDateUtil;
import com.wnc.sboot1.itbook.service.BookKpiService;
import com.wnc.sboot1.service.impl.MailService;

@RunWith( SpringRunner.class )
@SpringBootTest
public class MailServiceTest
{

    @Autowired
    private MailService mailService;
    @Autowired
    TemplateEngine templateEngine;
    @Autowired
    BookKpiService bookKpiService;

    @Test
    public void testSimpleMail() throws Exception
    {
        mailService.sendSimpleMail( "529801034@qq.com", "test simple mail",
                " hello this is simple mail" );
    }

    // @Test
    public void sendTemplateMail()
    {
        // 创建邮件正文
        Context context = new Context();
        context.setVariable( "summary", "【今日阅读统计】" );
        context.setVariable( "dataList",
                bookKpiService.lineWeek4Mail().subList( 6, 7 ) );
        String emailContent = templateEngine.process( "emailTemplate",
                context );
        System.out.println( emailContent );
        mailService.sendHtmlMail( "529801034@qq.com",
                "七天阅读统计 (" + BasicDateUtil.getCurrentDateString() + ")",
                emailContent );
    }
}
