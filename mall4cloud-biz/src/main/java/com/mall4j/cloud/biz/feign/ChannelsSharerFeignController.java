package com.mall4j.cloud.biz.feign;

import com.mall4j.cloud.api.biz.dto.ChannelsSharerDto;
import com.mall4j.cloud.api.biz.feign.ChannelsSharerFeign;
import com.mall4j.cloud.biz.service.channels.ChannelsSharerService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChannelsSharerFeignController implements ChannelsSharerFeign {
    @Autowired
    ChannelsSharerService channelsSharerService;

    @Override
    public ServerResponseEntity<Long> getStaffId(String openId) {
        return ServerResponseEntity.success(channelsSharerService.getSharerStaffIdByOpenId(openId));
    }

    @Override
    public ServerResponseEntity<List<ChannelsSharerDto>> getByOpenIds(List<String> openIds) {
        return ServerResponseEntity.success(channelsSharerService.getByOpenIds(openIds));
    }
}
