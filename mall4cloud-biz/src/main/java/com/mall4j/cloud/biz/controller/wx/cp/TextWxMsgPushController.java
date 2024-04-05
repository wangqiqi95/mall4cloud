package com.mall4j.cloud.biz.controller.wx.cp;

import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.biz.service.cp.handler.plus.ChatChangePlusHandler;
import com.mall4j.cloud.biz.service.cp.handler.plus.ContactChangePlusHandler;
import com.mall4j.cloud.biz.service.cp.handler.plus.ExternalContactChangePlusHandler;
import com.mall4j.cloud.biz.service.cp.handler.plus.MsgPlusHandler;
import com.mall4j.cloud.biz.util.TestWxMsgPushUtil;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.cp.bean.message.WxCpXmlMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 企业微信配置表
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
@RequiredArgsConstructor
@RestController("TextWxMsgPushController")
@RequestMapping("/p/cp/test/msg/push")
@Api(tags = "调试企微数据推送")
@Slf4j
@RefreshScope
public class TextWxMsgPushController {


//    @Value("${scrm.biz.openPushProdCpMsg}")
    private boolean openPushProdCpMsg=false;
    @Value("${scrm.biz.receviePushProdCpMsg}")
    private boolean receviePushProdCpMsg=false;

    private final ContactChangePlusHandler contactChangeHandler;
    private final ExternalContactChangePlusHandler externalContactChangeHandler;
    private final MsgPlusHandler msgHandler;
    private final ChatChangePlusHandler chatChangeHandler;//企微群消息
    private final TestWxMsgPushUtil testWxMsgPushUtil;//企微群消息

    @PostMapping("/ua/push")
    @ApiOperation(value = "push", notes = "push")
    public ServerResponseEntity<Void> push(@Valid @RequestBody WxCpXmlMessage wxMessage) {
        testWxMsgPushUtil.ExternalContactChangeHandler(JSON.toJSONString(wxMessage));
        return ServerResponseEntity.success();
    }

    @PostMapping("/ua/pushMsg")
    @ApiOperation(value = "push企微会话存档", notes = "push企微会话存档")
    public ServerResponseEntity<Void> push(HttpServletRequest request,
                                           @RequestBody() String sRespData,
                                           @RequestParam("msg_signature") String msgSignature,
                                           @RequestParam("timestamp") String timestamp,
                                           @RequestParam("nonce") String nonce) {
        if(!receviePushProdCpMsg){
            return ServerResponseEntity.success();
        }
        testWxMsgPushUtil.msgCallback(sRespData,msgSignature,timestamp,nonce);
        return ServerResponseEntity.success();
    }

    @PostMapping("/ua/ExternalContactChangeHandler")
    @ApiOperation(value = "externalContactChangeHandler", notes = "externalContactChangeHandler")
    public ServerResponseEntity<Void> externalContactChangeHandler(@Valid @RequestBody WxCpXmlMessage wxMessage) {
        if(!receviePushProdCpMsg){
            return ServerResponseEntity.success();
        }
        externalContactChangeHandler.handle(wxMessage,null,null,null);
        return ServerResponseEntity.success();
    }

    @PostMapping("/ua/MsgHandler")
    @ApiOperation(value = "msgHandler", notes = "msgHandler")
    public ServerResponseEntity<Void> msgHandler(@Valid @RequestBody WxCpXmlMessage wxMessage) {
        if(!receviePushProdCpMsg){
            return ServerResponseEntity.success();
        }
        msgHandler.handle(wxMessage,null,null,null);
        return ServerResponseEntity.success();
    }

    @PostMapping("/ua/ChatChangeHandler")
    @ApiOperation(value = "ChatChangeHandler", notes = "ChatChangeHandler")
    public ServerResponseEntity<Void> ChatChangeHandler(@Valid @RequestBody WxCpXmlMessage wxMessage) {
        if(!receviePushProdCpMsg){
            return ServerResponseEntity.success();
        }
        chatChangeHandler.handle(wxMessage,null,null,null);
        return ServerResponseEntity.success();
    }

    @PostMapping("/ua/ContactChangeHandler")
    @ApiOperation(value = "ContactChangeHandler", notes = "ContactChangeHandler")
    public ServerResponseEntity<Void> ContactChangeHandler(@Valid @RequestBody WxCpXmlMessage wxMessage) {
        if(!receviePushProdCpMsg){
            return ServerResponseEntity.success();
        }
        contactChangeHandler.handle(wxMessage,null,null,null);
        return ServerResponseEntity.success();
    }

}
