package com.mall4j.cloud.distribution.controller.staff;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.distribution.dto.DistributionWithdrawRecordDTO;
import com.mall4j.cloud.distribution.model.DistributionWithdrawRecord;
import com.mall4j.cloud.distribution.service.DistributionWithdrawRecordService;
import com.mall4j.cloud.distribution.vo.DistributionWithdrawRecordVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 佣金管理-佣金提现记录
 *
 * @author ZengFanChang
 * @date 2021-12-05 20:15:06
 */
@RestController("staffDistributionWithdrawRecordController")
@RequestMapping("/s/distribution_withdraw_record")
@Api(tags = "导购小程序-佣金管理-佣金提现记录")
public class DistributionWithdrawRecordController {

    @Autowired
    private DistributionWithdrawRecordService distributionWithdrawRecordService;

    @GetMapping("/page")
    @ApiOperation(value = "获取佣金管理-佣金提现记录列表", notes = "分页获取佣金管理-佣金提现记录列表")
    public ServerResponseEntity<PageVO<DistributionWithdrawRecordVO>> page(@Valid PageDTO pageDTO, DistributionWithdrawRecordDTO distributionWithdrawRecordDTO) {
        distributionWithdrawRecordDTO.setUserId(AuthUserContext.get().getUserId());
        distributionWithdrawRecordDTO.setIdentityType(1);
        distributionWithdrawRecordDTO.setStoreId(AuthUserContext.get().getStoreId());
        PageVO<DistributionWithdrawRecordVO> distributionWithdrawRecordPage = distributionWithdrawRecordService.page(pageDTO, distributionWithdrawRecordDTO);
        return ServerResponseEntity.success(distributionWithdrawRecordPage);
    }

    @GetMapping
    @ApiOperation(value = "获取佣金管理-佣金提现记录", notes = "根据id获取佣金管理-佣金提现记录")
    public ServerResponseEntity<DistributionWithdrawRecord> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(distributionWithdrawRecordService.getById(id));
    }

    @PostMapping("/applyWithdraw")
    @ApiOperation(value = "佣金管理-佣金提现申请", notes = "佣金管理-佣金提现申请")
    public ServerResponseEntity<Void> applyWithdraw(@RequestBody DistributionWithdrawRecordDTO distributionWithdrawRecordDTO) {
        distributionWithdrawRecordDTO.setUserId(AuthUserContext.get().getUserId());
        distributionWithdrawRecordDTO.setIdentityType(1);
        distributionWithdrawRecordService.applyWithdraw(distributionWithdrawRecordDTO);
        return ServerResponseEntity.success();
    }

    @PostMapping("/applyHistoryWithdraw")
    @ApiOperation(value = "佣金管理-历史佣金提现申请", notes = "佣金管理-历史佣金提现申请")
    public ServerResponseEntity<Void> applyHistoryWithdraw(@RequestBody DistributionWithdrawRecordDTO distributionWithdrawRecordDTO) {
        distributionWithdrawRecordDTO.setUserId(AuthUserContext.get().getUserId());
        distributionWithdrawRecordDTO.setIdentityType(1);
        distributionWithdrawRecordService.applyHistoryWithdraw(distributionWithdrawRecordDTO);
        return ServerResponseEntity.success();
    }

}
