package com.mall4j.cloud.distribution.controller.app;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.model.DistributionCommissionLog;
import com.mall4j.cloud.distribution.service.DistributionCommissionLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 佣金流水信息
 *
 * @author ZengFanChang
 * @date 2021-12-11 19:42:23
 */
@RestController("appDistributionCommissionLogController")
@RequestMapping("/distribution_commission_log")
@Api(tags = "佣金流水信息")
public class DistributionCommissionLogController {

    @Autowired
    private DistributionCommissionLogService distributionCommissionLogService;

    @GetMapping("/page")
    @ApiOperation(value = "获取佣金流水信息列表", notes = "分页获取佣金流水信息列表")
    public ServerResponseEntity<PageVO<DistributionCommissionLog>> page(@Valid PageDTO pageDTO) {
        PageVO<DistributionCommissionLog> distributionCommissionLogPage = distributionCommissionLogService.page(pageDTO);
        return ServerResponseEntity.success(distributionCommissionLogPage);
    }

    @GetMapping
    @ApiOperation(value = "获取佣金流水信息", notes = "根据id获取佣金流水信息")
    public ServerResponseEntity<DistributionCommissionLog> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(distributionCommissionLogService.getById(id));
    }

}
