package com.mall4j.cloud.platform.controller.platform;

import com.mall4j.cloud.api.platform.constant.OfflineHandleEventType;
import com.mall4j.cloud.api.platform.vo.OfflineHandleEventVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.platform.service.OfflineHandleEventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author lth
 * @Date 2021/5/21 15:41
 */
@RestController("platformOfflineHandleEventController")
@RequestMapping("/p/offline_handle_event")
@Api(tags = "platform-下线处理事件信息")
public class OfflineHandleEventController {

    @Autowired
    private OfflineHandleEventService offlineHandleEventService;

    @GetMapping("/getOfflineHandleEventByShopId/{shopId}")
    @ApiOperation(value = "通过店铺id获取下线信息", notes = "通过店铺id获取下线信息")
    public ServerResponseEntity<OfflineHandleEventVO> getOfflineHandleEventByShopId(@PathVariable("shopId") Long shopId) {
        OfflineHandleEventVO offlineHandleEvent = offlineHandleEventService.getProcessingEventByHandleTypeAndHandleId(OfflineHandleEventType.SHOP.getValue(), shopId);
        return ServerResponseEntity.success(offlineHandleEvent);
    }
}
