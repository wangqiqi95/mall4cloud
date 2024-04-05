package com.mall4j.cloud.api.biz.feign;

import com.mall4j.cloud.api.biz.vo.WxCpUserInfoVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author hwy
 * @Date 2022/01/18 10:28
 */
@FeignClient(value = "mall4cloud-biz",contextId = "wxCpOAuth2")
public interface WxCpAuth2FeignClient {

    /**
     * 获取用户详细信息
     * @param code 授权码
     * @return 数量
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/wxCpOAuth2/getUserInfo")
    ServerResponseEntity<WxCpUserInfoVO> getUserInfo(@RequestParam("code") String code) ;

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/wxCpOAuth2/getUserInfo2")
    ServerResponseEntity<WxCpUserInfoVO> getUserInfo2(@RequestParam("sessionKey")String sessionKey,@RequestParam("encryptedData") String encryptedData,@RequestParam("ivStr") String ivStr);

    /**
     * 获取用户详细信息
     * @param code 授权码
     * @return 数量
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/wxCpOAuth2/getUserInfoById")
    ServerResponseEntity<WxCpUserInfoVO> getUserInfoById(@RequestParam("userId") String userId) ;
}
