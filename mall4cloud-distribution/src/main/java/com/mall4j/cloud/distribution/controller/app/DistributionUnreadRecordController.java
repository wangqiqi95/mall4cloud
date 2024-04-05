package com.mall4j.cloud.distribution.controller.app;

import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.distribution.model.DistributionUnreadRecord;
import com.mall4j.cloud.distribution.service.DistributionUnreadRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分销推广-用户未读信息
 *
 * @author ZengFanChang
 * @date 2022-01-23 22:16:06
 */
@RestController("appDistributionUnreadRecordController")
@RequestMapping("/distribution_unread_record")
@Api(tags = "微客-分销推广-用户未读信息")
public class DistributionUnreadRecordController {

    @Autowired
    private DistributionUnreadRecordService distributionUnreadRecordService;


    @GetMapping("/getDistributionUnreadRecord")
    @ApiOperation(value = "获取分销推广-用户未读信息", notes = "获取分销推广-用户未读信息")
    public ServerResponseEntity<DistributionUnreadRecord> getDistributionUnreadRecord() {
        return ServerResponseEntity.success(distributionUnreadRecordService.getByUser(2, AuthUserContext.get().getUserId()));
    }
}
