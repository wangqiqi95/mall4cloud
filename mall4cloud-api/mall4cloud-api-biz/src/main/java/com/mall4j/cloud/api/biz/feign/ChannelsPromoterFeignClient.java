package com.mall4j.cloud.api.biz.feign;

import com.mall4j.cloud.api.biz.dto.channels.response.league.promoter.PromoterResp;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description 视频号达人
 * @Author axin
 * @Date 2023-05-06 13:40
 **/
@FeignClient(value = "mall4cloud-biz",contextId = "channelsPromoter")
public interface ChannelsPromoterFeignClient {
    /**
     * 根据视频号id获取达人信息
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/ec/channels/promoter/getPromoterByFinderId")
    ServerResponseEntity<PromoterResp> getPromoterByFinderId(@RequestParam("finderId")String finderId);
}
