package com.mall4j.cloud.api.platform.feign;

import com.mall4j.cloud.api.platform.constant.OfflineHandleEventType;
import com.mall4j.cloud.api.platform.dto.OfflineHandleEventDTO;
import com.mall4j.cloud.api.platform.vo.OfflineHandleEventVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author YXF
 * @date 2021/01/16
 */
@FeignClient(value = "mall4cloud-platform",contextId = "offlineHandleEvent")
public interface OfflineHandleEventFeignClient {

    /**
     * 新增下线事件记录
     * @param offlineHandleEventDto
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/offlineHandleEvent/save")
    ServerResponseEntity<Void> save(@RequestBody OfflineHandleEventDTO offlineHandleEventDto);

    /**
     *  通过handleId获取最新下线商品的事件
     * @param handleType
     * @param handleId
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/offlineHandleEvent/getProcessingEventByHandleTypeAndHandleId")
    ServerResponseEntity<OfflineHandleEventVO> getProcessingEventByHandleTypeAndHandleId(@RequestParam("handleType") Integer handleType, @RequestParam("handleId") Long handleId);

    /**
     * 审核下线事件
     * @param offlineHandleEventDto
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/offlineHandleEvent/auditOfflineEvent")
    ServerResponseEntity<Void> auditOfflineEvent(@RequestBody OfflineHandleEventDTO offlineHandleEventDto);

    /**
     * 违规事件提交审核
     * @param offlineHandleEventDto
     * @return
     */
    @PutMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/offlineHandleEvent/updateToApply")
    ServerResponseEntity<Void> updateToApply(@RequestBody OfflineHandleEventDTO offlineHandleEventDto);

    /**
     * 更新审核事件的状态
     * @param offlineHandleEventDto
     * @return
     */
    @PutMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/offlineHandleEvent/update")
    ServerResponseEntity<Void> update(@RequestBody OfflineHandleEventDTO offlineHandleEventDto);

    /**
     * 根据事件类型与事件id删除事件
     * @param handleType
     * @param handleId
     * @return
     */
    @DeleteMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/deleteByHandleTypeAndHandleId")
    ServerResponseEntity<Void> deleteByHandleTypeAndHandleId(@RequestParam("handleType") Integer handleType, @RequestParam("handleId") Long handleId);
}
