package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.dto.DistributionWithdrawConfigDTO;
import com.mall4j.cloud.distribution.model.DistributionWithdrawConfig;
import com.mall4j.cloud.distribution.service.DistributionWithdrawConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 佣金管理-佣金提现配置
 *
 * @author ZengFanChang
 * @date 2021-12-05 19:21:36
 */
@RestController("platformDistributionWithdrawConfigController")
@RequestMapping("/p/distribution_withdraw_config")
@Api(tags = "平台端-佣金管理-佣金提现配置")
public class DistributionWithdrawConfigController {

    @Autowired
    private DistributionWithdrawConfigService distributionWithdrawConfigService;

    @Autowired
    private MapperFacade mapperFacade;

    @GetMapping("/page")
    @ApiOperation(value = "获取佣金管理-佣金提现配置列表", notes = "分页获取佣金管理-佣金提现配置列表")
    public ServerResponseEntity<PageVO<DistributionWithdrawConfig>> page(@Valid PageDTO pageDTO) {
        PageVO<DistributionWithdrawConfig> distributionWithdrawConfigPage = distributionWithdrawConfigService.page(pageDTO);
        return ServerResponseEntity.success(distributionWithdrawConfigPage);
    }

    @GetMapping
    @ApiOperation(value = "获取佣金管理-佣金提现配置", notes = "根据id获取佣金管理-佣金提现配置")
    public ServerResponseEntity<DistributionWithdrawConfig> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(distributionWithdrawConfigService.getById(id));
    }


    @GetMapping("/getByIdentityType")
    @ApiOperation(value = "获取佣金管理-佣金提现配置", notes = "根据身份类型获取佣金管理-佣金提现配置")
    public ServerResponseEntity<DistributionWithdrawConfig> getByIdentityType(@RequestParam Integer identityType) {
        return ServerResponseEntity.success(distributionWithdrawConfigService.getByIdentityType(identityType));
    }

    @PostMapping
    @ApiOperation(value = "保存佣金管理-佣金提现配置", notes = "保存佣金管理-佣金提现配置")
    public ServerResponseEntity<Void> save(@Valid @RequestBody DistributionWithdrawConfigDTO distributionWithdrawConfigDTO) {
        DistributionWithdrawConfig distributionWithdrawConfig = mapperFacade.map(distributionWithdrawConfigDTO, DistributionWithdrawConfig.class);
        distributionWithdrawConfig.setId(null);
        if (distributionWithdrawConfigDTO.getIdentityType() == 2) {
            distributionWithdrawConfig.setCommissionSwitch(1);
        }
        distributionWithdrawConfigService.save(distributionWithdrawConfig);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新佣金管理-佣金提现配置", notes = "更新佣金管理-佣金提现配置")
    public ServerResponseEntity<Void> update(@Valid @RequestBody DistributionWithdrawConfigDTO distributionWithdrawConfigDTO) {
        DistributionWithdrawConfig distributionWithdrawConfig = mapperFacade.map(distributionWithdrawConfigDTO, DistributionWithdrawConfig.class);
        distributionWithdrawConfigService.update(distributionWithdrawConfig);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除佣金管理-佣金提现配置", notes = "根据佣金管理-佣金提现配置id删除佣金管理-佣金提现配置")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        distributionWithdrawConfigService.deleteById(id);
        return ServerResponseEntity.success();
    }
}
