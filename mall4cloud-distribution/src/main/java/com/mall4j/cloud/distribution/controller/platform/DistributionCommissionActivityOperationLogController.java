package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.model.DistributionCommissionActivityOperationLog;
import com.mall4j.cloud.distribution.service.DistributionCommissionActivityOperationLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 佣金配置-活动佣金-操作日志
 *
 * @author gww
 * @date 2021-12-16 16:04:31
 */
@RestController("multishopDistributionCommissionActivityOperationLogController")
@RequestMapping("/p/distribution_commission_activity_operation_log")
@Api(tags = "平台端-佣金配置-活动佣金-操作日志")
public class DistributionCommissionActivityOperationLogController {

    @Autowired
    private DistributionCommissionActivityOperationLogService distributionCommissionActivityOperationLogService;

	@GetMapping("/page")
	@ApiOperation(value = "获取佣金配置-活动佣金-操作日志列表", notes = "分页获取佣金配置-活动佣金-操作日志列表")
	public ServerResponseEntity<PageVO<DistributionCommissionActivityOperationLog>> page(@Valid PageDTO pageDTO,
																						 @RequestParam(required = true) Long activityId) {
		PageVO<DistributionCommissionActivityOperationLog> distributionCommissionActivityOperationLogPage =
                distributionCommissionActivityOperationLogService.page(pageDTO, activityId);
		return ServerResponseEntity.success(distributionCommissionActivityOperationLogPage);
	}
}
