package com.mall4j.cloud.api.biz.feign;

import com.mall4j.cloud.api.biz.dto.ChannelsSharerDto;
import com.mall4j.cloud.api.biz.dto.channels.response.EcOrderResponse;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "mall4cloud-biz",contextId = "channelsSharer")
public interface ChannelsSharerFeign {
    /**
     * 根据分享员opneid获取导购id
     * @return 成功or失败
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/ec/channelsSharer/getStaffId")
    ServerResponseEntity<Long> getStaffId(@RequestParam("openId")String openId);

    /**
     * 根据分享员opneid获取分享员信息
     *
     * @param openIds
     * @return boolean
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/ec/channelsSharer/getInfos")
    ServerResponseEntity<List<ChannelsSharerDto>> getByOpenIds(@RequestBody List<String> openIds);
}
