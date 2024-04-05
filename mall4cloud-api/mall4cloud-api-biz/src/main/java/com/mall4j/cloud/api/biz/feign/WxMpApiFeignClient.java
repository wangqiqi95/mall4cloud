package com.mall4j.cloud.api.biz.feign;

import com.mall4j.cloud.api.biz.vo.WeixinWebAppInfoVo;
import com.mall4j.cloud.api.user.vo.UserWeixinAccountFollowDataListVo;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author gmq
 * @Date 2022/02/14 15:19
 */
@FeignClient(value = "mall4cloud-biz", contextId = "wxMpApi")
public interface WxMpApiFeignClient {

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/wxmpapi/getWxMpInfo")
    ServerResponseEntity<WeixinWebAppInfoVo> getWxMpInfo(@RequestParam(value = "appId", required = true) String appId);


    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/wxmpapi/getWxMpAccessToken")
    ServerResponseEntity<String> getWxMpAccessToken(@RequestParam(value = "appId", required = true) String appId);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/wxmpapi/wechat/app/id")
    ServerResponseEntity<List<String>> listWechatAppIdById(@RequestParam(value = "ids", required = true) List<Long> ids);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/wxmpapi/wechat/account/log/fansDataByAppId")
    ServerResponseEntity<List<UserWeixinAccountFollowDataListVo>> fansDataByAppId(@RequestParam(value = "startTime", required = true) String startTime,
                                                                                  @RequestParam(value = "endTime", required = true) String endTime,
                                                                                  @RequestParam(value = "appId", required = true) String appId);

}
