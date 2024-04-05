package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.distribution.model.DistributionWithdrawProcessLog;
import com.mall4j.cloud.distribution.service.DistributionWithdrawProcessLogService;
import com.mall4j.cloud.distribution.vo.DistributionWithdrawProcessLogVO;
import com.mall4j.cloud.distribution.dto.DistributionWithdrawProcessLogDTO;

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
 * 佣金处理批次记录
 *
 * @author ZengFanChang
 * @date 2021-12-10 22:02:49
 */
@RestController("platformDistributionWithdrawProcessLogController")
@RequestMapping("/p/distribution_withdraw_process_log")
@Api(tags = "平台端-佣金处理批次记录")
public class DistributionWithdrawProcessLogController {

    @Autowired
    private DistributionWithdrawProcessLogService distributionWithdrawProcessLogService;

    @Autowired
	private MapperFacade mapperFacade;

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

    @PostMapping
    @ApiOperation(value = "保存佣金处理批次记录", notes = "保存佣金处理批次记录")
    public ServerResponseEntity<Void> save(@Valid @RequestBody DistributionWithdrawProcessLogDTO distributionWithdrawProcessLogDTO) {
        DistributionWithdrawProcessLog distributionWithdrawProcessLog = mapperFacade.map(distributionWithdrawProcessLogDTO, DistributionWithdrawProcessLog.class);
        distributionWithdrawProcessLog.setId(null);
        distributionWithdrawProcessLogService.save(distributionWithdrawProcessLog);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新佣金处理批次记录", notes = "更新佣金处理批次记录")
    public ServerResponseEntity<Void> update(@Valid @RequestBody DistributionWithdrawProcessLogDTO distributionWithdrawProcessLogDTO) {
        DistributionWithdrawProcessLog distributionWithdrawProcessLog = mapperFacade.map(distributionWithdrawProcessLogDTO, DistributionWithdrawProcessLog.class);
        distributionWithdrawProcessLogService.update(distributionWithdrawProcessLog);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除佣金处理批次记录", notes = "根据佣金处理批次记录id删除佣金处理批次记录")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        distributionWithdrawProcessLogService.deleteById(id);
        return ServerResponseEntity.success();
    }
}
