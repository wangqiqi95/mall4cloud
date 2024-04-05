package com.mall4j.cloud.biz.service;


import com.mall4j.cloud.common.response.ServerResponseEntity;

/**
 * @Date 2021年12月30日, 0030 14:48
 * @Created by eury
 */
public interface SendEmailService {

    ServerResponseEntity<String> sendEmailToFileUrl(String fileUrl,String fromSubject,String fromContent, String receiveEmail);

}
