package com.mall4j.cloud.api.biz.feign;

import com.mall4j.cloud.api.biz.dto.livestore.response.LiveRoomResponse;
import com.mall4j.cloud.api.biz.dto.livestore.response.OrderAddResponse;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author lt
 * @date 2020/11/23
 */
@FeignClient(value = "mall4cloud-biz", contextId = "liveroom")
public interface LiveRoomClient {

    /**
     * 根据直播间id获取直播间信息
     *
     * @param roomId 直播间id
     * @return 结果集
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/room/info")
    ServerResponseEntity<LiveRoomResponse> getRoomInfo(@RequestParam("roomId") String roomId);

}
