package com.mall4j.cloud.api.biz.feign;

import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author lth
 * @Date 2021/7/23 10:28
 */
@FeignClient(value = "mall4cloud-biz",contextId = "attachFile")
public interface AttachFileFeignClient {

    /**
     * 获取未读消息数量
     * @param shopId
     * @return 数量
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/attachFile/updateShopIdByUid")
    ServerResponseEntity<Void> updateShopIdByUid(@RequestParam("shopId") Long shopId);
}
