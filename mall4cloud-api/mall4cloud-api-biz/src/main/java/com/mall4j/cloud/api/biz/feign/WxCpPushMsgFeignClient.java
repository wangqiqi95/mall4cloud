package com.mall4j.cloud.api.biz.feign;

import com.mall4j.cloud.api.biz.dto.cp.NotifyMsgTemplateDTO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(value = "mall4cloud-biz",contextId = "wxCpPushMsg")
public interface WxCpPushMsgFeignClient {

    /**
     * 好友流失提醒
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/pushMsg/lossUserNotify")
    ServerResponseEntity<Void> lossUserNotify(@RequestBody NotifyMsgTemplateDTO dto);

    /**
     * 根进提醒
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/pushMsg/followNotify")
    ServerResponseEntity<Void> followNotify(@RequestBody NotifyMsgTemplateDTO dto);

    /**
     * 素材浏览提醒
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/pushMsg/materialNotify")
    ServerResponseEntity<Void> materialNotify(@RequestBody NotifyMsgTemplateDTO dto);

}
