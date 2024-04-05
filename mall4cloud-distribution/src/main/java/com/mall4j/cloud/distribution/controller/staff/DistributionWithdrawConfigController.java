package com.mall4j.cloud.distribution.controller.staff;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.model.DistributionWithdrawConfig;
import com.mall4j.cloud.distribution.service.DistributionWithdrawConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 佣金管理-佣金提现配置
 *
 * @author ZengFanChang
 * @date 2021-12-05 19:21:36
 */
@RestController("staffDistributionWithdrawConfigController")
@RequestMapping("/s/distribution_withdraw_config")
@Api(tags = "佣金管理-佣金提现配置")
public class DistributionWithdrawConfigController {

    @Autowired
    private DistributionWithdrawConfigService distributionWithdrawConfigService;

    @GetMapping("/page")
    @ApiOperation(value = "获取佣金管理-佣金提现配置列表", notes = "分页获取佣金管理-佣金提现配置列表")
    public ServerResponseEntity<PageVO<DistributionWithdrawConfig>> page(@Valid PageDTO pageDTO) {
        PageVO<DistributionWithdrawConfig> distributionWithdrawConfigPage = distributionWithdrawConfigService.page(pageDTO);
        return ServerResponseEntity.success(distributionWithdrawConfigPage);
    }

    @GetMapping("/getConfig")
    @ApiOperation(value = "获取佣金管理-佣金提现配置", notes = "根据id获取佣金管理-佣金提现配置")
    public ServerResponseEntity<DistributionWithdrawConfig> getConfig() {
        return ServerResponseEntity.success(distributionWithdrawConfigService.getByIdentityType(1));
    }
}
