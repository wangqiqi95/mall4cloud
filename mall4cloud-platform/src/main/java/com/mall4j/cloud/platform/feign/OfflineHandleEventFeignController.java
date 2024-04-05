package com.mall4j.cloud.platform.feign;

import com.mall4j.cloud.api.platform.dto.OfflineHandleEventDTO;
import com.mall4j.cloud.api.platform.feign.OfflineHandleEventFeignClient;
import com.mall4j.cloud.api.platform.vo.OfflineHandleEventVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.platform.model.OfflineHandleEvent;
import com.mall4j.cloud.platform.service.OfflineHandleEventService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author FrozenWatermelon
 * @date 2020/12/30
 */
@RestController
public class OfflineHandleEventFeignController implements OfflineHandleEventFeignClient {

    @Autowired
    private OfflineHandleEventService offlineHandleEventService;
    @Autowired
    private MapperFacade mapperFacade;

    @Override
    public ServerResponseEntity<Void> save(OfflineHandleEventDTO offlineHandleEventDto){
        int count = offlineHandleEventService.offlineCount(offlineHandleEventDto.getHandleType(), offlineHandleEventDto.getHandleId());
        if (count > 0) {
            throw new LuckException("该活动状态异常不能下线，请刷新页面！");
        }
        OfflineHandleEvent offlineHandleEvent = mapperFacade.map(offlineHandleEventDto, OfflineHandleEvent.class);
        offlineHandleEventService.save(offlineHandleEvent);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<OfflineHandleEventVO> getProcessingEventByHandleTypeAndHandleId(Integer handleType, Long handleId) {
        OfflineHandleEventVO offlineHandleEventVO = offlineHandleEventService.getProcessingEventByHandleTypeAndHandleId(handleType, handleId);
        return ServerResponseEntity.success(offlineHandleEventVO);
    }

    @Override
    public ServerResponseEntity<Void> auditOfflineEvent(OfflineHandleEventDTO offlineHandleEventDto) {
        offlineHandleEventService.auditOfflineEvent(offlineHandleEventDto);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> updateToApply(OfflineHandleEventDTO offlineHandleEventDto) {
        offlineHandleEventService.updateToApply(offlineHandleEventDto.getEventId(), offlineHandleEventDto.getReapplyReason());
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> update(OfflineHandleEventDTO offlineHandleEventDto) {
        offlineHandleEventService.update(offlineHandleEventDto);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> deleteByHandleTypeAndHandleId(Integer handleType, Long handleId) {
        offlineHandleEventService.deleteByHandleTypeAndHandleId(handleType, handleId);
        return ServerResponseEntity.success();
    }
}
