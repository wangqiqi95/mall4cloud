package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.biz.vo.ComponentAccessTokenVo;
import com.mall4j.cloud.biz.vo.PreAuthCodeVo;
import com.mall4j.cloud.common.response.ServerResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Date 2021年12月30日, 0030 14:48
 * @Created by eury
 */
public interface WechatOpMsgEventService {

    String doBackEvent(String xml,Map<String, String> resultMap,
                 HttpServletRequest request,
                 HttpServletResponse response) ;

}
