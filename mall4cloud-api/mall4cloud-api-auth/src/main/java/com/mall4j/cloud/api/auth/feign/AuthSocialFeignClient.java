package com.mall4j.cloud.api.auth.feign;

import com.mall4j.cloud.api.auth.dto.AuthAccountDTO;
import com.mall4j.cloud.api.auth.dto.AuthSocialDTO;
import com.mall4j.cloud.api.auth.vo.AuthSocialVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author FrozenWatermelon
 * @date 2020/9/22
 */
@FeignClient(value = "mall4cloud-auth", contextId = "auth-social")
public interface AuthSocialFeignClient {

    /**
     * 获取根据尝试社交登录时，保存的临时的uid获取社交
     *
     * @param tempUid tempUid
     * @return 用户社交账号信息
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/social")
    ServerResponseEntity<AuthSocialVO> getByTempUid(@RequestParam("tempUid") String tempUid);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/getByUnionId")
    ServerResponseEntity<AuthSocialVO> getByUnionId(@RequestParam("unionId") String unionId);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/getByOpenId")
    ServerResponseEntity<AuthSocialVO> getByOpenId(@RequestParam("openId") String openId);

    /**
     * 通过小程序信息获取用户手机号
     *
     * @param sessionKey    小程序session_key
     * @param encryptedData 微信小程序的encryptedData
     * @param ivStr         微信小程序的ivStr
     * @return mobile
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/social/getMobileByMaInfo")
    ServerResponseEntity<String> getMobileByMaInfo(@RequestParam("accessToken") String sessionKey, @RequestParam("encryptedData") String encryptedData, @RequestParam("ivStr") String ivStr);


    /**
     * 根据用户UID获取信息
     *
     * @param uid
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/getById")
    ServerResponseEntity<AuthSocialVO> getById(@RequestParam("id") Long id);

    /**
     * AuthSocialDTO账户信息保存
     * @param authSocialDTO 账户信息
     * @return Long uid
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/social")
    ServerResponseEntity<Void> save(@RequestBody AuthSocialDTO authSocialDTO);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/bingUidById")
    ServerResponseEntity<Void> bingUidById(@RequestParam("uid") Long uid,@RequestParam("id") Long id);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/updateUnionId")
    ServerResponseEntity<Void> updateUnionId(@RequestParam("userId") Long userId,@RequestParam("unionId") String unionId);


}
