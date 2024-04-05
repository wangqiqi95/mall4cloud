package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.dto.DistributionSpuLogDTO;
import com.mall4j.cloud.distribution.model.DistributionSpuLog;
import com.mall4j.cloud.distribution.service.DistributionSpuLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 分销商品浏览记录信息
 *
 * @author cl
 * @date 2021-08-09 14:14:07
 */
@RestController("platformDistributionSpuLogController")
@RequestMapping("/p/distribution_spu_log")
@Api(tags = "分销商品浏览记录信息")
public class DistributionSpuLogController {

    @Autowired
    private DistributionSpuLogService distributionSpuLogService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取分销商品浏览记录信息列表", notes = "分页获取分销商品浏览记录信息列表")
	public ServerResponseEntity<PageVO<DistributionSpuLog>> page(@Valid PageDTO pageDTO) {
		PageVO<DistributionSpuLog> distributionSpuLogPage = distributionSpuLogService.page(pageDTO);
		return ServerResponseEntity.success(distributionSpuLogPage);
	}

	@GetMapping
    @ApiOperation(value = "获取分销商品浏览记录信息", notes = "根据distributionSpuLogId获取分销商品浏览记录信息")
    public ServerResponseEntity<DistributionSpuLog> getByDistributionSpuLogId(@RequestParam Long distributionSpuLogId) {
        return ServerResponseEntity.success(distributionSpuLogService.getByDistributionSpuLogId(distributionSpuLogId));
    }

    @PostMapping
    @ApiOperation(value = "保存分销商品浏览记录信息", notes = "保存分销商品浏览记录信息")
    public ServerResponseEntity<Void> save(@Valid @RequestBody DistributionSpuLogDTO distributionSpuLogDTO) {
        DistributionSpuLog distributionSpuLog = mapperFacade.map(distributionSpuLogDTO, DistributionSpuLog.class);
        distributionSpuLog.setDistributionSpuLogId(null);
        distributionSpuLogService.save(distributionSpuLog);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新分销商品浏览记录信息", notes = "更新分销商品浏览记录信息")
    public ServerResponseEntity<Void> update(@Valid @RequestBody DistributionSpuLogDTO distributionSpuLogDTO) {
        DistributionSpuLog distributionSpuLog = mapperFacade.map(distributionSpuLogDTO, DistributionSpuLog.class);
        distributionSpuLogService.update(distributionSpuLog);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除分销商品浏览记录信息", notes = "根据分销商品浏览记录信息id删除分销商品浏览记录信息")
    public ServerResponseEntity<Void> delete(@RequestParam Long distributionSpuLogId) {
        distributionSpuLogService.deleteById(distributionSpuLogId);
        return ServerResponseEntity.success();
    }
}
