package cn.edu.fudan.biological.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * @program: biological
 * @description: 邮件工具类
 * @author: Yao Hongtao
 * @create: 2021-04-16 10:03
 **/
@Component
public class MailUtil {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    public MailUtil(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendCodeMail(String to, String code) throws MailException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("生物知情同意小程序验证码");
        String text = "尊敬的用户：\n\r\t您本次的验证码为" + code + "，有效期为5分钟，请保密并尽快验证。";
        message.setText(text);

        mailSender.send(message);
    }


}

