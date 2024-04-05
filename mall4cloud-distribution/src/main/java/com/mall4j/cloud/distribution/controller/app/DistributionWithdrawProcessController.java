package com.mall4j.cloud.distribution.controller.app;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.dto.DistributionWithdrawProcessDTO;
import com.mall4j.cloud.distribution.model.DistributionWithdrawProcess;
import com.mall4j.cloud.distribution.service.DistributionWithdrawProcessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 佣金处理批次
 *
 * @author ZengFanChang
 * @date 2021-12-10 22:02:48
 */
@RestController("appDistributionWithdrawProcessController")
@RequestMapping("/distribution_withdraw_process")
@Api(tags = "佣金处理批次")
public class DistributionWithdrawProcessController {

    @Autowired
    private DistributionWithdrawProcessService distributionWithdrawProcessService;

    @GetMapping("/page")
    @ApiOperation(value = "获取佣金处理批次列表", notes = "分页获取佣金处理批次列表")
    public ServerResponseEntity<PageVO<DistributionWithdrawProcess>> page(@Valid PageDTO pageDTO, DistributionWithdrawProcessDTO dto) {
        PageVO<DistributionWithdrawProcess> distributionWithdrawProcessPage = distributionWithdrawProcessService.page(pageDTO, dto);
        return ServerResponseEntity.success(distributionWithdrawProcessPage);
    }

    @GetMapping
    @ApiOperation(value = "获取佣金处理批次", notes = "根据id获取佣金处理批次")
    public ServerResponseEntity<DistributionWithdrawProcess> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(distributionWithdrawProcessService.getById(id));
    }
}
