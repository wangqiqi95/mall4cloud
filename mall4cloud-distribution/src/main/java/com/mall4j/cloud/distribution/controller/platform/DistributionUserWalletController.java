package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.distribution.dto.DistributionUserWalletDTO;
import com.mall4j.cloud.distribution.model.DistributionUserWallet;
import com.mall4j.cloud.distribution.service.DistributionUserWalletService;
import com.mall4j.cloud.distribution.vo.DistributionUserWalletVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 分销员钱包信息
 *
 * @author cl
 * @date 2021-08-09 14:14:10
 */
@RestController("platformDistributionUserWalletController")
@RequestMapping("/p/distribution_user_wallet")
@Api(tags = "分销员钱包信息")
public class DistributionUserWalletController {

    @Autowired
    private DistributionUserWalletService distributionUserWalletService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取分销员钱包信息列表", notes = "分页获取分销员钱包信息列表")
	public ServerResponseEntity<PageVO<DistributionUserWallet>> page(@Valid PageDTO pageDTO) {
		PageVO<DistributionUserWallet> distributionUserWalletPage = distributionUserWalletService.page(pageDTO);
		return ServerResponseEntity.success(distributionUserWalletPage);
	}

    @GetMapping("/wallet_page")
    @ApiOperation(value = "获取分销员钱包信息列表", notes = "分页获取分销员钱包信息列表")
    public ServerResponseEntity<PageVO<DistributionUserWalletVO>> walletPage(@Valid PageDTO pageDTO,String userMobile) {
        PageVO<DistributionUserWalletVO> distributionUserWalletPage = distributionUserWalletService.walletPage(pageDTO, userMobile);
        return ServerResponseEntity.success(distributionUserWalletPage);
    }

	@GetMapping
    @ApiOperation(value = "获取分销员钱包信息", notes = "根据walletId获取分销员钱包信息")
    public ServerResponseEntity<DistributionUserWallet> getByWalletId(@RequestParam Long walletId) {
        return ServerResponseEntity.success(distributionUserWalletService.getByWalletId(walletId));
    }

    @PutMapping
    @ApiOperation(value = "更新分销员钱包信息", notes = "更新分销员钱包信息")
    public ServerResponseEntity<Void> update(@Valid @RequestBody DistributionUserWalletDTO distributionUserWalletDTO) {
        Long userId = AuthUserContext.get().getUserId();
        distributionUserWalletService.updateWallet(distributionUserWalletDTO, userId);
        return ServerResponseEntity.success();
    }

}
