package com.mall4j.cloud.biz.feign;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall4j.cloud.api.biz.dto.livestore.response.LiveRoomResponse;
import com.mall4j.cloud.api.biz.feign.LiveRoomClient;
import com.mall4j.cloud.biz.model.live.LiveRoom;
import com.mall4j.cloud.biz.service.live.LiveRoomService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cl
 * @date 2021-08-13 15:50:02
 */
@RestController
@Slf4j
public class LiveRoomFeignController implements LiveRoomClient {

    @Autowired
    private LiveRoomService liveRoomService;

    @Override
    public ServerResponseEntity<LiveRoomResponse> getRoomInfo(String roomId) {
        LiveRoom byId = liveRoomService.getOne(new LambdaQueryWrapper<LiveRoom>().eq(LiveRoom::getRoomId, roomId));
        log.info("LiveRoomFeignController get by roomId = {}", byId);
        LiveRoomResponse response = new LiveRoomResponse();
        if (byId != null) {
            BeanUtils.copyProperties(byId, response);
        }
        return ServerResponseEntity.success(response);
    }
}
