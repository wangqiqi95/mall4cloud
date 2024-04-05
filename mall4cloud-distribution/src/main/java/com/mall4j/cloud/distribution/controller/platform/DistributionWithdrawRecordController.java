package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.distribution.model.DistributionWithdrawRecord;
import com.mall4j.cloud.distribution.service.DistributionWithdrawRecordService;
import com.mall4j.cloud.distribution.vo.DistributionWithdrawRecordVO;
import com.mall4j.cloud.distribution.dto.DistributionWithdrawRecordDTO;

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

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 佣金管理-佣金提现记录
 *
 * @author ZengFanChang
 * @date 2021-12-05 20:15:06
 */
@RestController("platformDistributionWithdrawRecordController")
@RequestMapping("/p/distribution_withdraw_record")
@Api(tags = "平台端-佣金管理-佣金提现记录")
public class DistributionWithdrawRecordController {

    @Autowired
    private DistributionWithdrawRecordService distributionWithdrawRecordService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取佣金管理-佣金提现记录列表", notes = "分页获取佣金管理-佣金提现记录列表")
	public ServerResponseEntity<PageVO<DistributionWithdrawRecordVO>> page(@Valid PageDTO pageDTO, DistributionWithdrawRecordDTO distributionWithdrawRecordDTO) {
		PageVO<DistributionWithdrawRecordVO> distributionWithdrawRecordPage = distributionWithdrawRecordService.page(pageDTO, distributionWithdrawRecordDTO);
		return ServerResponseEntity.success(distributionWithdrawRecordPage);
	}

	@GetMapping
    @ApiOperation(value = "获取佣金管理-佣金提现记录", notes = "根据id获取佣金管理-佣金提现记录")
    public ServerResponseEntity<DistributionWithdrawRecord> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(distributionWithdrawRecordService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "保存佣金管理-佣金提现记录", notes = "保存佣金管理-佣金提现记录")
    public ServerResponseEntity<Void> save(@Valid @RequestBody DistributionWithdrawRecordDTO distributionWithdrawRecordDTO) {
        DistributionWithdrawRecord distributionWithdrawRecord = mapperFacade.map(distributionWithdrawRecordDTO, DistributionWithdrawRecord.class);
        distributionWithdrawRecord.setId(null);
        distributionWithdrawRecordService.save(distributionWithdrawRecord);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新佣金管理-佣金提现记录", notes = "更新佣金管理-佣金提现记录")
    public ServerResponseEntity<Void> update(@Valid @RequestBody DistributionWithdrawRecordDTO distributionWithdrawRecordDTO) {
        DistributionWithdrawRecord distributionWithdrawRecord = mapperFacade.map(distributionWithdrawRecordDTO, DistributionWithdrawRecord.class);
        distributionWithdrawRecordService.update(distributionWithdrawRecord);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除佣金管理-佣金提现记录", notes = "根据佣金管理-佣金提现记录id删除佣金管理-佣金提现记录")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        distributionWithdrawRecordService.deleteById(id);
        return ServerResponseEntity.success();
    }

    @PostMapping("/processWithdraw")
    @ApiOperation(value = "佣金管理-佣金提现审核", notes = "佣金管理-佣金提现审核")
    public ServerResponseEntity<Void> processWithdraw(@RequestBody DistributionWithdrawRecordDTO distributionWithdrawRecordDTO){
        distributionWithdrawRecordService.processWithdraw(distributionWithdrawRecordDTO);
        return ServerResponseEntity.success();
    }

    @ApiOperation(value = "佣金提现记录导出")
    @GetMapping("/withdrawRecordExcel")
    public ServerResponseEntity<Void> withdrawRecordExcel(HttpServletResponse response, DistributionWithdrawRecordDTO dto){
	    distributionWithdrawRecordService.withdrawRecordExcel(response, dto);
        return ServerResponseEntity.success();
    }

}
