package com.mall4j.cloud.api.biz.feign;

import com.mall4j.cloud.api.biz.dto.ma.WxMaGenerateSchemeRequest;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author gmq
 * @Date 2022/02/14 15:19
 */
@FeignClient(value = "mall4cloud-biz",contextId = "wxMaApi")
public interface WxMaApiFeignClient {

    /**
     * 小程序
     * 授权获取手机号
     * @param code
     * @param encryptedData
     * @param ivStr
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/wxmaapi/getPhoneNumber")
    ServerResponseEntity<String> getPhoneNumber(@RequestParam(value = "code",required = true) String code,
                                                @RequestParam(value = "encryptedData",required = false) String encryptedData,
                                                @RequestParam(value = "ivStr",required = false) String ivStr,
                                                @ApiParam(value = "授权方式：1:新版 2:旧版",required = true)
                                                @RequestParam(value = "authType",required = false,defaultValue ="1") Integer authType) ;


    /**
     * 根据发送类型查询订阅模版
     * @param sendType 发送类型
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/wxmaapi/getSubscriptTmessage")
    ServerResponseEntity<WeixinMaSubscriptTmessageVO> getSubscriptTmessage(@RequestParam(value = "sendType",required = true) Integer sendType,
                                                                           @RequestParam(value = "bussinessIds",required = false) List<String> bussinessIds);

    /**
     * 根据发送类型查询订阅模版
     * @param sendType 发送类型
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/wxmaapi/getSubscriptTmessage")
    ServerResponseEntity<WeixinMaSubscriptTmessageVO> getinsIderSubscriptTmessage(@RequestParam(value = "sendType",required = true) Integer sendType,
                                                                           @RequestParam(value = "bussinessIds",required = false) List<String> bussinessIds);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/wxmaapi/updateUserRecordId")
    ServerResponseEntity<Void> updateUserRecordId(@RequestParam(value = "userRecordId",required = true) Long userRecordId);

    /**
     * 取消用户的订阅记录
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/wxmaapi/cancelUserSubscriptRecord")
    ServerResponseEntity<Void> cancelUserSubscriptRecord(@RequestParam(value = "userId",required = true) Long userId,
                                                         @RequestParam(value = "sendType",required = true) Integer sendType,
                                                         @RequestParam(value = "bussinessId",required = true) Long bussinessIds);


    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/wxmaapi/getScoreProductSubscriptTmessage")
    ServerResponseEntity<WeixinMaSubscriptTmessageVO> getScoreProductSubscriptTmessage(@RequestParam(value = "sendType",required = true) Integer sendType,
                                                                                       @RequestParam(value = "bussinessId",required = true) String bussinessId);

    /**
     * 生成scheme码
     * @param request
     * @return
     */
    @PostMapping(FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX +"/insider/wxmaapi/generateScheme")
    ServerResponseEntity<String> generateScheme(@RequestBody WxMaGenerateSchemeRequest request);
}
