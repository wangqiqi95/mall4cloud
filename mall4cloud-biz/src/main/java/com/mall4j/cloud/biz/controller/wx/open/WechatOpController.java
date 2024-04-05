package com.mall4j.cloud.biz.controller.wx.open;

import com.mall4j.cloud.biz.wx.wx.util.SHA1;
import com.mall4j.cloud.biz.service.WechatOpService;
import com.mall4j.cloud.biz.vo.ComponentAccessTokenVo;
import com.mall4j.cloud.biz.vo.PreAuthCodeVo;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.error.YAMLException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @Date 2021年12月30日, 0030 15:18
 * @Created by eury
 *
 * 接口执行顺序：
 * 1. 开放平台配置第三方平台开发资料：授权流程、授权后业务、白名单
 * 2. 启动票据推送服务
 * 3. 获取令牌第三方平台接口的调用凭据
 * 4. 获取预授权码
 * 5. 获取授权链接
 */
@RestController
@RequestMapping("/ua/wechat/op")
@Api(tags = "微信开放平台服务")
public class WechatOpController {

    private static final Logger log = LoggerFactory.getLogger(WechatOpController.class);

    @Resource
    private WechatOpService wechatOpService;

    /**
     * 操作方法：
     * 1.开放平台申请第三方平台账号
     * 2.第三方平台账号配置开发资料，全网发布
     * 3. 调用接口：获取令牌第三方平台接口的调用凭据 /getComponentAccessToken
     * 4. 调用接口：获取预授权码 /getPreAuthCode
     * 5.
     */

    /**
     * 微信开放平台授权事件接收URL
     * 用于接收平台推送给第三方平台账号的消息与事件，如授权事件通知、component_verify_ticket等。
     * 注意，该URL的一级域名需与“消息与事件接收URL”的一级域名一致
     * 开放平台-管理中心-第三方平台-详情-开发信息-开发资料
     * https://open.weixin.qq.com/wxaopen/createThirdAccount/modifyDevInfo?appId=wx00bc5c32f45d33e3&token=5d50c33a346c0e6d4334a4f24cd3c19a97f9957c
     * <p>
     * 1、配置授权事件URL，用于接收component_verify_ticket
     * 出于安全考虑，【在第三方平台创建审核通过后】，微信服务器 每隔 10 分钟会向第三方的消息接收地址推送一次 component_verify_ticket，用于获取第三方平台接口调用凭据。omponent_verify_ticket有效期为12h
     *
     * @param signature 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @param echostr   随机字符串
     * @param postdata  消息体
     * @return java.lang.String
     */
    @RequestMapping("/authEvent")
    @ApiOperation(value = "微信开放平台授权事件接收URL")
    public String authEvent(@RequestParam(name = "signature") String signature,
                            @RequestParam(name = "timestamp") String timestamp,
                            @RequestParam(name = "nonce") String nonce,
                            @RequestParam(name = "echostr", required = false) String echostr,
                            @RequestParam(name = "encrypt_type", required = false) String encryptType,
                            @RequestParam(name = "msg_signature", required = false) String msgSignature,
                            @RequestBody(required = false) String postdata) {

        return wechatOpService.authEvent(signature, timestamp, nonce, echostr, encryptType, msgSignature, postdata);
    }

    /**
     * 微信开放平台消息事件接收URL
     * 用于代授权的公众号或小程序的接收平台推送的消息与事件，该参数按规则填写（需包含/$APPID$，如www.abc.com/$APPID$/callback），实际接收消息时$APPID$将被替换为公众号或小程序AppId。
     * 注意：该URL的一级域名需与“授权事件接收URL”的一级域名一致
     * 开放平台-管理中心-第三方平台-详情-开发信息-开发资料
     * https://open.weixin.qq.com/wxaopen/createThirdAccount/modifyDevInfo?appId=wx00bc5c32f45d33e3&token=5d50c33a346c0e6d4334a4f24cd3c19a97f9957c
     *
     * @param signature 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @param echostr   随机字符串
     * @param postData  消息体
     * @return java.lang.String
     */
    @RequestMapping("/msgEvent/{APPID}")
    @ApiOperation(value = "微信开放平台消息事件接收URL")
    public void msgEvent(@PathVariable(name = "APPID") String appId,
                         @RequestParam(name = "signature") String signature,
                         @RequestParam(name = "timestamp") String timestamp,
                         @RequestParam(name = "nonce") String nonce,
                         @RequestParam(name = "openid", required = false) String openid,
                         @RequestParam(name = "echostr", required = false) String echostr,
                         @RequestParam(name = "msg_signature", required = false) String msgSignature,
                         @RequestBody(required = false) String postData,
                         HttpServletRequest request, HttpServletResponse response) {

        try {
            log.info("【logger微信开放平台消息事件接收URL】请求参数：appId：【{}】，signature：【{}】，timestamp：【{}】，nonce：【{}】，openid：【{}】，echostr：【{}】，msgSignature：【{}】，postdata：【{}】",appId, signature, timestamp, nonce, openid, echostr, msgSignature, postData);
            String result = wechatOpService.msgEvent(appId, signature, timestamp, nonce, openid, echostr, msgSignature, postData, request, response);
            log.info("msgEvent  back---->"+result);
            PrintWriter pw = response.getWriter();
            pw.write(result);
            pw.flush();
            pw.close();
//            return result;
        } catch (Exception e) {
            log.error("返回消息解析失败", e);
//            return WechatConstants.FAILED;
        }
    }

    /**
     * 启动票据推送服务
     *
     * @param
     * @return Response
     */
    @GetMapping("/apiStartPushTicket")
    @ApiOperation(value = "启动票据推送服务", notes = "微信鉴权说明", httpMethod = "GET")
    public ServerResponseEntity apiStartPushTicket() {
        return wechatOpService.apiStartPushTicket();
    }

    /**
     * 获取令牌
     *
     * @param
     * @return Response
     */
    @PostMapping("/getComponentAccessToken")
    @ApiOperation("获取令牌第三方平台接口的调用凭据")
    public ServerResponseEntity getComponentAccessToken() {
        return wechatOpService.getComponentAccessToken();
    }

    /**
     * 获取预授权码
     *
     * @return Response
     */
    @ApiOperation("获取预授权码")
    @PostMapping("/getPreAuthCode")
    public ServerResponseEntity getPreAuthCode() {
        return wechatOpService.getPreAuthCode();
    }

    /**
     * 获取授权链接
     *
     * @return
     */
    @ApiOperation("获取授权链接")
    @PostMapping("/getScanCodeAuthUrl")
    public ServerResponseEntity getScanCodeAuthUrl() {
        return wechatOpService.getScanCodeAuthUrl();
    }

    /**
     * 授权回调接口
     *
     * @return
     */
    @ApiOperation("扫码授权回调")
    @RequestMapping(value = "scanCodecallback",method = {RequestMethod.GET,RequestMethod.POST})
    public String getAuthAccessToken(@RequestParam("auth_code") String authCode,
                                     @RequestParam("expires_in") Long expiresIn) {
        //授权处理
        ServerResponseEntity authAccessToken = wechatOpService.scanCodecallback(authCode, expiresIn);
        if ("00000".equals(authAccessToken.getCode())){
            return "恭喜您，授权成功！";
        }
        return "授权失败，请稍后重试！";
    }

    /**
     * 获取刷新授权公众号或小程序的接口调用凭据
     *
     * @return
     */
    @ApiOperation("获取刷新授权公众号或小程序的接口调用凭据")
    @PostMapping("/getAuthRefreshToken")
    public ServerResponseEntity getAuthRefreshToken() {
        return wechatOpService.getAuthRefreshToken();
    }


}
