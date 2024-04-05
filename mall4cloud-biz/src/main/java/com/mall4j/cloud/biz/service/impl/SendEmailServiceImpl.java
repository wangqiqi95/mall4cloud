package com.mall4j.cloud.biz.service.impl;


import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.biz.service.SendEmailService;
import com.mall4j.cloud.biz.util.SendEmail;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.error.YAMLException;

/**
 * 消息事件推送处理
 * @Date 2021年12月30日, 0030 14:50
 * @Created by eury
 */
@Slf4j
@Service
public class SendEmailServiceImpl implements SendEmailService {

    @Autowired
    private SendEmail sendEmail;
    @Value(value = "${spring.mail.subject}")
    private String subject; //发件者的邮箱
    @Value(value = "${spring.mail.content}")
    private String content; //发件者的邮箱

    @Override
    public ServerResponseEntity<String> sendEmailToFileUrl(String fileUrl, String fromSubject,String fromContent,String receiveEmail) {
        if(StrUtil.isBlank(fileUrl)){
            log.info("邮件发送失败 -->  发送内容为空");
            return ServerResponseEntity.showFailMsg("邮件发送失败 发送内容为空");
        }
        if(StrUtil.isBlank(receiveEmail)){
            log.info("邮件发送失败 -->  接收者邮件为空");
            return ServerResponseEntity.showFailMsg("邮件发送失败 接收者邮件为空");
        }
        try {
            subject=StrUtil.isNotBlank(fromSubject)?fromSubject:subject;
            content=StrUtil.isNotBlank(fromContent)?fromContent:content;
            String toUser=receiveEmail;
            log.info("邮件发送内容  subject->【{}】  content->【{}】 receiveEmail->【{}】 fileUrl->【{}】 ",subject,content,receiveEmail,fileUrl);
            String fileName=fileUrl.substring(fileUrl.lastIndexOf("/")+1,fileUrl.length());
            sendEmail.sendRemoteFileMail(toUser,subject,content,fileUrl,fileName);
            return ServerResponseEntity.success("邮件已发送");
        }catch (Exception e){
            log.info(""+e.getMessage());
            return ServerResponseEntity.showFailMsg("邮件发送失败");
        }
    }
}
