package com.mall4j.cloud.biz.feign;

import com.mall4j.cloud.api.biz.feign.AttachFileFeignClient;
import com.mall4j.cloud.biz.service.AttachFileGroupService;
import com.mall4j.cloud.biz.service.AttachFileService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author lth
 * @Date 2021/7/23 10:30
 */
@RestController
public class AttachFileFeignController implements AttachFileFeignClient {

    @Autowired
    private AttachFileService attachFileService;
    @Autowired
    private AttachFileGroupService attachFileGroupService;

    @Override
    public ServerResponseEntity<Void> updateShopIdByUid(Long shopId) {
        attachFileService.updateShopIdByUid(shopId, AuthUserContext.get().getUid());
        attachFileGroupService.updateShopIdByUid(shopId, AuthUserContext.get().getUid());
        return ServerResponseEntity.success();
    }

}
