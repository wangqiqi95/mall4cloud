package com.mall4j.cloud.biz.controller.wx.cp;


import com.mall4j.cloud.biz.config.WxCpConfigurationPlus;
import com.mall4j.cloud.common.util.Json;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.message.WxCpXmlMessage;
import me.chanjar.weixin.cp.bean.message.WxCpXmlOutMessage;
import me.chanjar.weixin.cp.util.crypto.WxCpCryptUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 企业微信配回调类
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
@Slf4j
@RestController
@RequestMapping("/ua/cp/portal/{agentId}")
public class WxCpPortalController {


  @GetMapping(produces = "text/plain;charset=utf-8")
  public void authGet(@PathVariable Integer agentId,
                        @RequestParam(name = "msg_signature", required = false) String signature,
                        @RequestParam(name = "timestamp", required = false) String timestamp,
                        @RequestParam(name = "nonce", required = false) String nonce,
                        @RequestParam(name = "echostr", required = false) String echostr,
                        HttpServletResponse response) {
    log.info("\n接收到来自微信服务器的认证消息：agentId=[{}],signature = [{}], timestamp = [{}], nonce = [{}], echostr = [{}]",
            agentId, signature, timestamp, nonce, echostr);
    String  result = "";
    if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
      throw new IllegalArgumentException("请求参数非法，请核实!");
    }
    final WxCpService wxCpService = WxCpConfigurationPlus.getWxCpService(agentId);
    if (wxCpService == null) {
      throw new IllegalArgumentException(String.format("未找到对应suiteId=[%d]的配置，请核实！", agentId));
    }
    log.info("===Token=="+wxCpService.getWxCpConfigStorage().getToken()+"==AesKey=="+wxCpService.getWxCpConfigStorage().getAesKey());
    if (wxCpService.checkSignature(signature, timestamp, nonce, echostr)) {
        result = new WxCpCryptUtil(wxCpService.getWxCpConfigStorage()).decrypt(echostr);
    }
    log.info("===result=="+result);
    try {
       PrintWriter pw = response.getWriter();
       pw.write(result);
       pw.flush();
       pw.close();
     }catch (Exception e) {
       log.error("消息解析失败", e);
     }
    //return "非法请求";
  }

  @PostMapping(produces = "application/xml; charset=UTF-8")
  public void post(@PathVariable Integer agentId,
                     @RequestBody String requestBody,
                     @RequestParam("msg_signature") String signature,
                     @RequestParam("timestamp") String timestamp,
                     @RequestParam("nonce") String nonce,
                     HttpServletResponse response) {
    log.info("\n接收微信请求：[signature=[{}], timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ", signature, timestamp, nonce, requestBody);
    final WxCpService wxCpService = WxCpConfigurationPlus.getWxCpService(agentId);
    WxCpXmlMessage inMessage = WxCpXmlMessage.fromEncryptedXml(requestBody, wxCpService.getWxCpConfigStorage(), timestamp, nonce, signature);
    log.debug("\n消息解密后内容为：\n{} ", Json.toJsonString(inMessage));
    WxCpXmlOutMessage outMessage = this.route(agentId, inMessage);
    if (outMessage == null) {
      return ;
    }
    String out = outMessage.toEncryptedXml(wxCpService.getWxCpConfigStorage());
    log.debug("\n组装回复信息：{}", out);
    try {
      PrintWriter pw = response.getWriter();
      pw.write(out);
      pw.flush();
      pw.close();
    }catch (Exception e) {
      log.error("消息解析失败", e);
    }
  }

  private WxCpXmlOutMessage route(Integer agentId, WxCpXmlMessage message) {
    try {
      return WxCpConfigurationPlus.getRouters().get(agentId).route(message);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }


}
