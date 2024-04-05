package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.distribution.model.DistributionWithdrawProcess;
import com.mall4j.cloud.distribution.service.DistributionWithdrawProcessService;
import com.mall4j.cloud.distribution.vo.DistributionWithdrawProcessVO;
import com.mall4j.cloud.distribution.dto.DistributionWithdrawProcessDTO;

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
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * 佣金处理批次
 *
 * @author ZengFanChang
 * @date 2021-12-10 22:02:48
 */
@RestController("platformDistributionWithdrawProcessController")
@RequestMapping("/p/distribution_withdraw_process")
@Api(tags = "平台端-佣金处理批次")
public class DistributionWithdrawProcessController {

    @Autowired
    private DistributionWithdrawProcessService distributionWithdrawProcessService;

    @Autowired
	private MapperFacade mapperFacade;

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

    @PostMapping
    @ApiOperation(value = "保存佣金处理批次", notes = "保存佣金处理批次")
    public ServerResponseEntity<Void> save(@Valid @RequestBody DistributionWithdrawProcessDTO distributionWithdrawProcessDTO) {
        DistributionWithdrawProcess distributionWithdrawProcess = mapperFacade.map(distributionWithdrawProcessDTO, DistributionWithdrawProcess.class);
        distributionWithdrawProcess.setId(null);
        distributionWithdrawProcessService.save(distributionWithdrawProcess);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新佣金处理批次", notes = "更新佣金处理批次")
    public ServerResponseEntity<Void> update(@Valid @RequestBody DistributionWithdrawProcessDTO distributionWithdrawProcessDTO) {
        DistributionWithdrawProcess distributionWithdrawProcess = mapperFacade.map(distributionWithdrawProcessDTO, DistributionWithdrawProcess.class);
        distributionWithdrawProcessService.update(distributionWithdrawProcess);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除佣金处理批次", notes = "根据佣金处理批次id删除佣金处理批次")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        distributionWithdrawProcessService.deleteById(id);
        return ServerResponseEntity.success();
    }


    @ApiOperation(value = "导入文件")
    @PostMapping("/importExcel")
    public ServerResponseEntity<String> importExcel(@RequestParam("excelFile") MultipartFile file, @RequestParam("name") String name){
        if(file == null) {
            throw new LuckException("网络繁忙，请稍后重试");
        }
        String info = distributionWithdrawProcessService.importUserExcel(file, name);
        return ServerResponseEntity.success(info);
    }

    @ApiOperation(value = "执行批量提现处理")
    @PostMapping("/execute")
    public ServerResponseEntity<Void> execute(@RequestParam("id") Long id){
	    distributionWithdrawProcessService.execute(id);
        return ServerResponseEntity.success();
    }
}
