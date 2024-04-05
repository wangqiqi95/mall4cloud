package com.mall4j.cloud.platform.controller.multishop;

import com.mall4j.cloud.api.platform.constant.OfflineHandleEventType;
import com.mall4j.cloud.api.platform.vo.OfflineHandleEventVO;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.platform.service.OfflineHandleEventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @Author lth
 * @Date 2021/5/24 18:26
 */
@RestController("multishopOfflineHandleEventController")
@RequestMapping("/m/offline_handle_event")
@Api(tags = "multishop-下线处理事件信息")
public class OfflineHandleEventController {

    @Autowired
    private OfflineHandleEventService offlineHandleEventService;

    @GetMapping("/get_event_info")
    @ApiOperation(value = "通过店铺id获取下线信息", notes = "通过店铺id获取下线信息")
    public ServerResponseEntity<OfflineHandleEventVO> getOfflineHandleEventByShopId() {
        Long shopId = AuthUserContext.get().getTenantId();
        if (Objects.equals(shopId, Constant.DEFAULT_SHOP_ID)) {
            return ServerResponseEntity.success();
        }
        OfflineHandleEventVO offlineHandleEvent = offlineHandleEventService.getProcessingEventByHandleTypeAndHandleId(OfflineHandleEventType.SHOP.getValue(), shopId);
        return ServerResponseEntity.success(offlineHandleEvent);
    }
}
