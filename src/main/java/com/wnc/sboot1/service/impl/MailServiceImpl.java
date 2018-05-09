
package com.wnc.sboot1.service.impl;

import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class MailServiceImpl implements MailService
{

    private final Logger logger = Logger.getLogger( this.getClass() );

    @Autowired
    private JavaMailSender mailSender;

    @Value( "${mail.fromMail.addr}" )
    private String from;

    @Override
    public void sendSimpleMail( String to, String subject, String content )
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom( from );
        message.setTo( to );
        message.setSubject( subject );
        message.setText( content );

        try
        {
            mailSender.send( message );
            logger.info( "简单邮件已经发送。" );
        } catch ( Exception e )
        {
            logger.error( "发送简单邮件时发生异常！", e );
        }

    }

    public synchronized boolean sendHtmlMail( String to, String subject,
            String content )
    {
        try
        {
            MimeMessage message = mailSender.createMimeMessage();

            // true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper( message, true );
            helper.setFrom( from );
            helper.setTo( to );
            helper.setSubject( subject );
            helper.setText( content, true );

            mailSender.send( message );
            logger.info( "html邮件发送成功" );
            return true;
        } catch ( Exception e )
        {
            logger.error( "发送html邮件时发生异常！", e );
        }
        return false;
    }
}