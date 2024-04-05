package com.mall4j.cloud.distribution.controller.app;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.distribution.dto.DistributionWithdrawRecordDTO;
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
@RestController("appDistributionWithdrawRecordController")
@RequestMapping("/distribution_withdraw_record")
@Api(tags = "微客-佣金管理-佣金提现")
public class DistributionWithdrawRecordController {

    @Autowired
    private DistributionWithdrawRecordService distributionWithdrawRecordService;

    @GetMapping("/page")
    @ApiOperation(value = "佣金提现记录", notes = "佣金提现记录")
    public ServerResponseEntity<PageVO<DistributionWithdrawRecordVO>> page(@Valid PageDTO pageDTO, DistributionWithdrawRecordDTO distributionWithdrawRecordDTO) {
        distributionWithdrawRecordDTO.setUserId(AuthUserContext.get().getUserId());
        distributionWithdrawRecordDTO.setIdentityType(2);
        distributionWithdrawRecordDTO.setStoreId(AuthUserContext.get().getStoreId());
        PageVO<DistributionWithdrawRecordVO> distributionWithdrawRecordPage = distributionWithdrawRecordService.page(pageDTO, distributionWithdrawRecordDTO);
        return ServerResponseEntity.success(distributionWithdrawRecordPage);
    }

    @PostMapping("/applyWithdraw")
    @ApiOperation(value = "佣金提现申请", notes = "佣金提现申请")
    public ServerResponseEntity<Void> applyWithdraw(@RequestBody DistributionWithdrawRecordDTO distributionWithdrawRecordDTO) {
        distributionWithdrawRecordDTO.setUserId(AuthUserContext.get().getUserId());
        distributionWithdrawRecordDTO.setIdentityType(2);
        distributionWithdrawRecordService.applyWithdraw(distributionWithdrawRecordDTO);
        return ServerResponseEntity.success();
    }

    @GetMapping("/getWithdrawNeedRefundAmount")
    @ApiOperation(value = "获取已提现需退金额", notes = "获取已提现需退金额")
    public ServerResponseEntity<Long> getWithdrawNeedRefundAmount() {
        return ServerResponseEntity.success(distributionWithdrawRecordService.getWithdrawNeedRefundAmount(2, AuthUserContext.get().getUserId()));
    }

    @PostMapping("/updateWithdrawRecordBankCard")
    @ApiOperation(value = "更改提现记录银行卡信息", notes = "更改提现记录银行卡信息")
    public ServerResponseEntity<Void> updateWithdrawRecordBankCard(@RequestParam("applyId") String applyId, @RequestParam("cardNo") String cardNo) {
        distributionWithdrawRecordService.updateWithdrawRecordBankCard(applyId, cardNo);
        return ServerResponseEntity.success();
    }

    @PostMapping("/applyHistoryWithdraw")
    @ApiOperation(value = "历史佣金提现申请", notes = "历史佣金提现申请")
    public ServerResponseEntity<Void> applyHistoryWithdraw(@RequestBody DistributionWithdrawRecordDTO distributionWithdrawRecordDTO) {
        distributionWithdrawRecordDTO.setUserId(AuthUserContext.get().getUserId());
        distributionWithdrawRecordDTO.setIdentityType(2);
        distributionWithdrawRecordService.applyHistoryWithdraw(distributionWithdrawRecordDTO);
        return ServerResponseEntity.success();
    }

}
