package com.mall4j.cloud.biz.feign;

import com.mall4j.cloud.api.biz.dto.cp.NotifyMsgTemplateDTO;
import com.mall4j.cloud.api.biz.feign.WxCpPushMsgFeignClient;
import com.mall4j.cloud.biz.service.cp.WxCpPushService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * 发送消息feign
 */
@RestController
public class WxCpPushMsgController implements WxCpPushMsgFeignClient {

    @Autowired
    private WxCpPushService pushService;

    @Override
    public ServerResponseEntity<Void> lossUserNotify(NotifyMsgTemplateDTO dto) {
        pushService.lossUserNotify(dto);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> followNotify(NotifyMsgTemplateDTO dto) {
        pushService.followNotify(dto);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> materialNotify(NotifyMsgTemplateDTO dto) {
        pushService.materialNotify(dto);
        return ServerResponseEntity.success();
    }
}
