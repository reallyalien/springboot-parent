package com.ot.springboot.util;

import com.ot.springboot.domain.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailUtil {

    private static final Logger log= LoggerFactory.getLogger(MailUtil.class);

    @Value("${spring.mail.from}")
    private String from;
    @Autowired
    private JavaMailSender javaMailSender;


    public boolean send(Mail mail){
        String to = mail.getTo();
        String title = mail.getTitle();
        String content = mail.getContent();
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setText(content);
        simpleMailMessage.setSubject(title);
        try {
            javaMailSender.send(simpleMailMessage);
            log.info("邮件发送成功");
            return true;
        }catch (Exception e){
            log.error("邮件发送失败,to:{},title:{}",to,title,e);
            return false;
        }
    }
}
