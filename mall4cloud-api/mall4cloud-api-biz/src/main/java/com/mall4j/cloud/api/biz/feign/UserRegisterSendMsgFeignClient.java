package com.mall4j.cloud.api.biz.feign;

import com.mall4j.cloud.api.biz.dto.UserRegisterQiWeiMsgDTO;
import com.mall4j.cloud.api.biz.vo.WxCpUserInfoVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author hwy
 * @Date 2022/01/18 10:28
 */
@FeignClient(value = "mall4cloud-biz",contextId = "userRegisterSendMsg")
public interface UserRegisterSendMsgFeignClient {
    /**
     *推送会员注册消息
     * @param userRegisterQiWeiMsgDTO 会员信息
     * @return void
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/cp/notify/userRegisterSuccess")
    ServerResponseEntity<Void> userRegisterSuccessNotify(@RequestBody UserRegisterQiWeiMsgDTO userRegisterQiWeiMsgDTO) ;
}
