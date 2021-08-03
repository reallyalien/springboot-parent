package com.ot.springboot.mail.controller;

import com.ot.springboot.mail.domain.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@RestController
@RequestMapping("/sendMail")
public class SendMailController {

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender javaMailSender;


    @GetMapping("/simple")
    public String sendSimpleMail() {
        Mail mail = new Mail();
        mail.setContent("测试简单邮件发送");
        mail.setSubject("测试");
        mail.setTos(new String[]{"1754241423@qq.com"});
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(mail.getTos());
        message.setSubject(mail.getSubject());
        message.setText(mail.getContent());
        try {
            javaMailSender.send(message);
            return "邮件发送成功";
        } catch (MailException e) {
            e.printStackTrace();
            return "邮件发送失败";
        }
    }

    @GetMapping("/html")
    public String sendHtmlMail() {
        MimeMessage message = null;
        try {
            Mail mail = new Mail();
            String content = "<html>\n" +
                    "<body>\n" +
                    "    <h1>这是Html格式邮件!,不信你看邮件，我字体比一般字体还要大</h1>\n" +
                    "</body>\n" +
                    "</html>";
            mail.setContent(content);
            mail.setSubject("测试");
            mail.setTos(new String[]{"1754241423@qq.com"});
            message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(mail.getTos());
            helper.setSubject(mail.getSubject());
            helper.setText(mail.getContent(), true);
            javaMailSender.send(message);
            return "邮件发送成功";
        } catch (MessagingException e) {
            return "邮件发送失败";
        }
    }

    @GetMapping("/staticResource")
    public String sendStaticMail() {
        MimeMessage message = null;
        try {
            Mail mail = new Mail();
            String resId="1";
            String content =
                    "<html><body><img width='250px' src=\'cid:" + resId + "\'>"
                            + "</body></html>";
            mail.setContent(content);
            mail.setSubject("测试");
            mail.setTos(new String[]{"1754241423@qq.com"});
            message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(mail.getTos());
            helper.setSubject(mail.getSubject());
            helper.setText(mail.getContent(), true);
            //添加一个内联资源
            helper.addInline(resId,new File("D:/document/壁纸/1.jpg"));
            javaMailSender.send(message);
            return "邮件发送成功";
        } catch (MessagingException e) {
            return "邮件发送失败";
        }
    }
    @GetMapping("/attachment")
    public String sendAttachmentMail() {
        MimeMessage message = null;
        try {
            Mail mail = new Mail();
            String resId="1";
            String attachmentId="2";
            String content =
                    "<html><body><img width='250px' src=\'cid:" + resId + "\'>"
                            + "</body></html>";
            mail.setContent(content);
            mail.setSubject("测试");
            mail.setTos(new String[]{"1754241423@qq.com"});
            message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(mail.getTos());
            helper.setSubject(mail.getSubject());
            helper.setText(mail.getContent(), true);
            //添加一个内联资源
            helper.addInline(resId,new File("D:/document/壁纸/1.jpg"));
            //添加一个附件
            helper.addAttachment(attachmentId,new File("D:\\document\\前端\\1.txt"));
            javaMailSender.send(message);
            return "邮件发送成功";
        } catch (MessagingException e) {
            return "邮件发送失败";
        }
    }
}
