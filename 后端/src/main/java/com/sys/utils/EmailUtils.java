package com.sys.utils;

import com.sys.exception.AppException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.Session;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.UUID;

public class EmailUtils {

    public static void sendValidateCode(String email) {

        String text = UUID.randomUUID().toString().replace("-", "").substring(5, 9);
        RedisUtils.set(email, text);
        RedisUtils.setExpire(email, 5 * 60L);
        try {
            send(email, text);
        } catch (Exception e) {
            throw new AppException(StatusEnum.EMAIL_SEND_FAILED.getCode(), e.getMessage());
        }
    }

    //	指定接收信箱，内容与 邮箱发送原因(1-->验证码，2-->反馈)
    private static void send(String address, String text)
            throws UnsupportedEncodingException {
        //邮箱帐号 ticketsystemtestu	密码 ticketsystemp
        JavaMailSenderImpl simpl = new JavaMailSenderImpl();
        simpl.setHost("smtp.163.com");
        simpl.setPort(25);
        simpl.setUsername("ticketsystemtestu@163.com");
        simpl.setPassword("ticketsystem110");
        Properties mail = System.getProperties();
        mail.put("mail.smtp.auth", "true");
        simpl.setJavaMailProperties(mail);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(address);
        String name = MimeUtility.encodeText("验证码");
        message.setFrom(name + " <ticketsystemtestu@163.com>");
        message.setSubject("验证码");
        message.setText("您的验证码为:" + text + "  ,(5分钟内有效。)");
        simpl.send(message);
    }

}
