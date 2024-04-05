package com.mall4j.cloud.distribution.controller.app;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.model.DistributionWithdrawProcessLog;
import com.mall4j.cloud.distribution.service.DistributionWithdrawProcessLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 佣金处理批次记录
 *
 * @author ZengFanChang
 * @date 2021-12-10 22:02:49
 */
@RestController("appDistributionWithdrawProcessLogController")
@RequestMapping("/distribution_withdraw_process_log")
@Api(tags = "佣金处理批次记录")
public class DistributionWithdrawProcessLogController {

    @Autowired
    private DistributionWithdrawProcessLogService distributionWithdrawProcessLogService;


    @GetMapping("/page")
    @ApiOperation(value = "获取佣金处理批次记录列表", notes = "分页获取佣金处理批次记录列表")
    public ServerResponseEntity<PageVO<DistributionWithdrawProcessLog>> page(@Valid PageDTO pageDTO) {
        PageVO<DistributionWithdrawProcessLog> distributionWithdrawProcessLogPage = distributionWithdrawProcessLogService.page(pageDTO);
        return ServerResponseEntity.success(distributionWithdrawProcessLogPage);
    }

    @GetMapping
    @ApiOperation(value = "获取佣金处理批次记录", notes = "根据id获取佣金处理批次记录")
    public ServerResponseEntity<DistributionWithdrawProcessLog> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(distributionWithdrawProcessLogService.getById(id));
    }
}
