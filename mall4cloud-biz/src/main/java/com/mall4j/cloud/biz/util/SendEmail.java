package com.mall4j.cloud.biz.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.activation.URLDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @Date 2022年3月10日, 0010 10:23
 * @Created by eury
 */
@Slf4j
@Component
public class SendEmail {

    @Autowired
    private JavaMailSender mailSender;

    @Value(value = "${spring.mail.username}")
    private String from; //发件者的邮箱

    /**
     * 发送远程文件邮件
     * @param to				收件人
     * @param subject			邮件主题
     * @param content	   		邮件内容
     * @param filePath			远程文件路径
     * @param fileName			下载后的文件名
     * @throws Exception
     */
    public void sendRemoteFileMail(String to, String subject, String content, String filePath, String fileName) throws Exception {

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            message.setContent(getMultipart(content, filePath, fileName));

            mailSender.send(message);

            //日志信息
            log.info("邮件已经发送。");
        } catch (MessagingException e) {
            log.error("发送邮件时发生异常！", e);
        }
    }

    /**
     * 获取Multipart对象，包含正文内容、附件、文件名
     * @return
     */
    private static Multipart getMultipart(String content, String remoteFile, String fileName) throws Exception {
        Multipart multipart = new MimeMultipart();
        // 向multipart中添加正文
        BodyPart contentBodyPart = new MimeBodyPart();
        contentBodyPart.setContent(content, "text/html;charset=UTF-8");
        multipart.addBodyPart(contentBodyPart);

        // 向multipart中添加远程附件
        multipart.addBodyPart(getBodyPart(encodeURI(remoteFile), fileName));

        return multipart;
    }

    /**
     * 获取 bodyPart
     * @param path 文件路径
     * @param fileName 文件名称
     * @return
     */
    public static BodyPart getBodyPart(String path,String fileName) throws Exception{
        BodyPart bodyPart = new MimeBodyPart();
        // 根据附件路径获取文件,
        DataHandler dh = null;
        dh = new DataHandler(new URLDataSource(new URL(path)));
        bodyPart.setDataHandler(dh);

        System.out.println(fileName);
        bodyPart.setFileName(fileName);
        return bodyPart;
    }

    public static String encodeURI(String url) throws Exception
    {
        String encode = URLEncoder.encode(url, "UTF-8");

        return encode.replace("%3A",":").replace("%2F","/");
    }
}

