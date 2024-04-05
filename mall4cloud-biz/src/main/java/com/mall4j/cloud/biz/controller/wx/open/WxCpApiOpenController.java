package com.mall4j.cloud.biz.controller.wx.open;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.feign.WxCpApiFeignClient;
import com.mall4j.cloud.biz.config.WxCpConfigurationPlus;
import com.mall4j.cloud.biz.dto.cp.WxJsapiSignatureDTO;
import com.mall4j.cloud.biz.service.cp.ConfigService;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.util.RandomUtils;
import me.chanjar.weixin.common.util.crypto.SHA1;
import me.chanjar.weixin.cp.bean.WxCpAgentJsapiSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/wx/cp")
@Api(tags = "微信-企微服务")
public class WxCpApiOpenController {

    @Autowired
    private ConfigService configService;
    @Autowired
    private WxCpApiFeignClient wxCpApiFeignClient;

    private final static String config_getJSSDK="scrm_biz:config_jssdk:";
    private final static String agentConfig_getJSSDK="scrm_biz:agentconfig_jssdk:";

    @PostMapping("/ua/config/getJSSDK")
    @ApiOperation(value = "移动端JS-SDK签名config", notes = "移动端JS-SDK签名config")
    public ServerResponseEntity<WxJsapiSignature> getJSSDKByConfig(@RequestBody WxJsapiSignatureDTO dto) {
        try {
            String key=config_getJSSDK+dto.getParames();
            if(RedisUtil.hasKey(key) && (Objects.nonNull(dto.getCache()) && dto.getCache()==1)){
                WxJsapiSignature jsapiSignature=JSONObject.parseObject(RedisUtil.get(key),WxJsapiSignature.class);
                log.info("config getJSSDK redis:{}",jsapiSignature.toString());
                return ServerResponseEntity.success(jsapiSignature);
            }
            String parames=dto.getParames();
            long timestamp = System.currentTimeMillis() / 1000;
            String noncestr = RandomUtils.getRandomStr();
            String jsapiTicket = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.CP_CONNECT_AGENT_ID).getJsapiTicket(false);
            String signature = SHA1.genWithAmple(
                    "jsapi_ticket=" + jsapiTicket,
                    "noncestr=" + noncestr,
                    "timestamp=" + timestamp,
                    "url=" + parames
            );
            WxJsapiSignature jsapiSignature = new WxJsapiSignature();
            jsapiSignature.setTimestamp(timestamp);
            jsapiSignature.setNonceStr(noncestr);
            jsapiSignature.setUrl(parames);
            jsapiSignature.setSignature(signature);
            // Fixed bug
            jsapiSignature.setAppId(configService.getConfig().getCpId());

//            String jsapiTicket="";
//            WxJsapiSignature jsapiSignature= WxCpConfiguration.getWxCpService(WxCpConfiguration.CP_CONNECT_AGENT_ID).createJsapiSignature(parames);

            log.info("移动端JS-SDK签名config获取结果: {} jsapiTicket: {}",jsapiSignature.toString(),jsapiTicket);

            RedisUtil.set(key, JSON.toJSONString(jsapiSignature), 600);
            return ServerResponseEntity.success(jsapiSignature);
        }catch (WxErrorException e){
            throw new LuckException(ResponseEnum.EXCEPTION);
        }
    }

    @PostMapping("/ua/agentConfig/getJSSDK")
    @ApiOperation(value = "移动端JS-SDK签名agentConfig", notes = "移动端JS-SDK签名agentConfig")
    public ServerResponseEntity<WxCpAgentJsapiSignature> getJSSDKAgentConfig(@RequestBody WxJsapiSignatureDTO dto) {
        try {
            String key=agentConfig_getJSSDK+dto.getParames();
            if(RedisUtil.hasKey(key) && (Objects.nonNull(dto.getCache()) && dto.getCache()==1)){
                WxCpAgentJsapiSignature jsapiSignature=JSONObject.parseObject(RedisUtil.get(key),WxCpAgentJsapiSignature.class);
                log.info("agentConfig getJSSDK redis:{}",jsapiSignature.toString());
                return ServerResponseEntity.success(jsapiSignature);
            }
            String parames=dto.getParames();
            long timestamp = System.currentTimeMillis() / 1000;
            String noncestr = RandomUtils.getRandomStr();
            ServerResponseEntity<String> responseEntity= wxCpApiFeignClient.wxCpAgentJsapiTicket();
            ServerResponseEntity.checkResponse(responseEntity);
            String jsapiTicket=responseEntity.getData();
            String signature = SHA1.genWithAmple(
                    "jsapi_ticket=" + jsapiTicket,
                    "noncestr=" + noncestr,
                    "timestamp=" + timestamp,
                    "url=" + parames
            );
            WxCpAgentJsapiSignature jsapiSignature = new WxCpAgentJsapiSignature();
            jsapiSignature.setTimestamp(timestamp);
            jsapiSignature.setNonceStr(noncestr);
            jsapiSignature.setUrl(parames);
            jsapiSignature.setSignature(signature);
            jsapiSignature.setCorpid(configService.getConfig().getCpId());
            jsapiSignature.setAgentid(configService.getConfig().getAgentId());

//            WxCpAgentJsapiSignature jsapiSignature= WxCpConfiguration.getWxCpService(WxCpConfiguration.getAgentId()).createAgentJsapiSignature(parames);

            log.info("移动端JS-SDK签名agentConfig获取结果: {} jsapiTicket: {}",jsapiSignature.toString(),jsapiTicket);
            RedisUtil.set(key, JSON.toJSONString(jsapiSignature), 600);
            return ServerResponseEntity.success(jsapiSignature);
//        }catch (WxErrorException e){
        }catch (Exception e){
            throw new LuckException(ResponseEnum.EXCEPTION);
        }
    }

}
