package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.distribution.model.DistributionCommissionLog;
import com.mall4j.cloud.distribution.service.DistributionCommissionLogService;
import com.mall4j.cloud.distribution.vo.DistributionCommissionLogVO;
import com.mall4j.cloud.distribution.dto.DistributionCommissionLogDTO;

import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

/**
 * 佣金流水信息
 *
 * @author ZengFanChang
 * @date 2021-12-11 19:42:23
 */
@RestController("platformDistributionCommissionLogController")
@RequestMapping("/p/distribution_commission_log")
@Api(tags = "平台端-佣金流水信息")
public class DistributionCommissionLogController {

    @Autowired
    private DistributionCommissionLogService distributionCommissionLogService;

    @Autowired
	private MapperFacade mapperFacade;

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

    @PostMapping
    @ApiOperation(value = "保存佣金流水信息", notes = "保存佣金流水信息")
    public ServerResponseEntity<Void> save(@Valid @RequestBody DistributionCommissionLogDTO distributionCommissionLogDTO) {
        DistributionCommissionLog distributionCommissionLog = mapperFacade.map(distributionCommissionLogDTO, DistributionCommissionLog.class);
        distributionCommissionLog.setId(null);
        distributionCommissionLogService.save(distributionCommissionLog);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新佣金流水信息", notes = "更新佣金流水信息")
    public ServerResponseEntity<Void> update(@Valid @RequestBody DistributionCommissionLogDTO distributionCommissionLogDTO) {
        DistributionCommissionLog distributionCommissionLog = mapperFacade.map(distributionCommissionLogDTO, DistributionCommissionLog.class);
        distributionCommissionLogService.update(distributionCommissionLog);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除佣金流水信息", notes = "根据佣金流水信息id删除佣金流水信息")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        distributionCommissionLogService.deleteById(id);
        return ServerResponseEntity.success();
    }
}
