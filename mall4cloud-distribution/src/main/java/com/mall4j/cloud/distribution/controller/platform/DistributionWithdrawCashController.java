package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.dto.DistributionWithdrawCashDTO;
import com.mall4j.cloud.distribution.model.DistributionUser;
import com.mall4j.cloud.distribution.model.DistributionUserWallet;
import com.mall4j.cloud.distribution.model.DistributionWithdrawCash;
import com.mall4j.cloud.distribution.service.DistributionUserService;
import com.mall4j.cloud.distribution.service.DistributionUserWalletService;
import com.mall4j.cloud.distribution.service.DistributionWithdrawCashService;
import com.mall4j.cloud.distribution.vo.DistributionWithdrawCashVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.Objects;

/**
 * 分销员提现记录
 *
 * @author cl
 * @date 2021-08-09 14:14:10
 */
@RestController("platformDistributionWithdrawCashController")
@RequestMapping("/p/distribution_withdraw_cash")
@Api(tags = "分销员提现记录")
public class DistributionWithdrawCashController {

    @Autowired
    private DistributionWithdrawCashService distributionWithdrawCashService;
    @Autowired
    private DistributionUserWalletService distributionUserWalletService;
    @Autowired
    private DistributionUserService distributionUserService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取分销员提现记录列表", notes = "分页获取分销员提现记录列表")
	public ServerResponseEntity<PageVO<DistributionWithdrawCashVO>> page(@Valid PageDTO pageDTO, String userMobile, DistributionWithdrawCashDTO distributionWithdrawCashDTO) {
		PageVO<DistributionWithdrawCashVO> distributionWithdrawCashPage = distributionWithdrawCashService.page(pageDTO, userMobile, distributionWithdrawCashDTO);
		return ServerResponseEntity.success(distributionWithdrawCashPage);
	}


    @PutMapping("/to_success")
    @ApiOperation(value = "更新分销员提现记录", notes = "更新分销员提现记录")
    public ServerResponseEntity<Void> update(Long withdrawCashId) {
        DistributionWithdrawCash distributionWithdrawCash = distributionWithdrawCashService.getByWithdrawCashId(withdrawCashId);
        if (distributionWithdrawCash == null) {
            // 无法获取提现信息
            throw new LuckException("无法获取提现信息");
        }
        Long walletId = distributionWithdrawCash.getWalletId();
        DistributionUserWallet wallet = distributionUserWalletService.getByWalletId(walletId);
        if (wallet == null) {
            // 无法获取用户钱包信息
            throw new LuckException("无法获取用户钱包信息");
        }
        DistributionUser distributionUser = distributionUserService.getByDistributionUserId(wallet.getDistributionUserId());
        if (distributionUser == null) {
            // 无法获取用户信息
            throw new LuckException("无法获取用户信息");
        }
        distributionWithdrawCash.setState(1);
        distributionWithdrawCashService.update(distributionWithdrawCash);
        return ServerResponseEntity.success();
    }

}
