
package com.wnc.sboot1.service.impl;

public interface MailService
{
    public void sendSimpleMail( String to, String subject, String content );

    public boolean sendHtmlMail( String to, String subject, String content );
}