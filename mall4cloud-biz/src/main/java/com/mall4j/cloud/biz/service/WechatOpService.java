package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.biz.vo.ComponentAccessTokenVo;
import com.mall4j.cloud.biz.vo.PreAuthCodeVo;
import com.mall4j.cloud.common.response.ServerResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Date 2021年12月30日, 0030 14:48
 * @Created by eury
 */
public interface  WechatOpService {

    /**
     * 授权事件接收URL
     * 用于接收平台推送给第三方平台账号的消息与事件，如授权事件通知、component_verify_ticket等。
     * 注意，该URL的一级域名需与“消息与事件接收URL”的一级域名一致
     *
     * @param signature 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @param echostr   随机字符串
     * @param postdata  消息体
     * @return 如果获得只需要返回 SUCCESS
     */
    String authEvent(String signature, String timestamp, String nonce, String echostr, String encryptType, String msgSignature, String postdata);

    /**
     * 消息事件接收URL
     * 用于代授权的公众号或小程序的接收平台推送的消息与事件，该参数按规则填写（需包含/$APPID$，如www.abc.com/$APPID$/callback），实际接收消息时$APPID$将被替换为公众号或小程序AppId。
     * 注意：该URL的一级域名需与“授权事件接收URL”的一级域名一致
     *
     * @param signature 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @param echostr   随机字符串
     * @param postData  消息体
     * @return 如果获得只需要返回 SUCCESS
     */
    String msgEvent(String appId,
                    String signature,
                    String timestamp,
                    String nonce,
                    String openid,
                    String echostr,
                    String msgSignature,
                    String postData,
                    HttpServletRequest request,
                    HttpServletResponse response);

    /**
     * 启动票据推送服务
     *
     * @return
     */
    ServerResponseEntity apiStartPushTicket();

    /**
     * 获取令牌，第三方平台接口的调用凭据
     *
     * @param componentAccessTokenVo
     * @return
     */
    ServerResponseEntity getComponentAccessToken();

    /**
     * 获取预授权码
     *
     * @param preAuthCodeVo
     * @return
     */
    ServerResponseEntity getPreAuthCode();

    /**
     * 获取授权链接
     *
     * @return
     */
    ServerResponseEntity getScanCodeAuthUrl();

    /**
     * 扫码授权回调
     * 用户扫码授权获取授权码，AuthAccessToken，AuthRefreshToken
     *
     * @return
     */
    ServerResponseEntity scanCodecallback(String authCode, Long expiresIn);

    /**
     * 获取刷新授权公众号或小程序的接口调用凭据
     *
     * @param
     * @return Response
     */
    ServerResponseEntity getAuthRefreshToken();
}
