package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.dto.DistributionUserWalletBillDTO;
import com.mall4j.cloud.distribution.model.DistributionUserWalletBill;
import com.mall4j.cloud.distribution.service.DistributionUserWalletBillService;
import com.mall4j.cloud.distribution.vo.DistributionUserWalletBillVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 分销员钱包流水记录
 *
 * @author cl
 * @date 2021-08-09 14:14:10
 */
@RestController("platformDistributionUserWalletBillController")
@RequestMapping("/p/distribution_user_wallet_bill")
@Api(tags = "分销员钱包流水记录")
public class DistributionUserWalletBillController {

    @Autowired
    private DistributionUserWalletBillService distributionUserWalletBillService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取分销员钱包流水记录列表", notes = "分页获取分销员钱包流水记录列表")
	public ServerResponseEntity<PageVO<DistributionUserWalletBill>> page(@Valid PageDTO pageDTO) {
		PageVO<DistributionUserWalletBill> distributionUserWalletBillPage = distributionUserWalletBillService.page(pageDTO);
		return ServerResponseEntity.success(distributionUserWalletBillPage);
	}

    @GetMapping("/wallet_bill_page")
    @ApiOperation(value = "获取分销员钱包流水记录列表", notes = "分页获取分销员钱包流水记录列表")
    public ServerResponseEntity<PageVO<DistributionUserWalletBillVO>> walletBillPage(@Valid PageDTO pageDTO, String userMobile) {
        PageVO<DistributionUserWalletBillVO> pageVO = distributionUserWalletBillService.walletBillPage(pageDTO, userMobile);
        return ServerResponseEntity.success(pageVO);
    }

	@GetMapping
    @ApiOperation(value = "获取分销员钱包流水记录", notes = "根据id获取分销员钱包流水记录")
    public ServerResponseEntity<DistributionUserWalletBill> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(distributionUserWalletBillService.getById(id));
    }

    @PutMapping
    @ApiOperation(value = "更新分销员钱包流水记录", notes = "更新分销员钱包流水记录")
    public ServerResponseEntity<Void> update(@Valid @RequestBody DistributionUserWalletBillDTO distributionUserWalletBillDTO) {
        DistributionUserWalletBill distributionUserWalletBill = mapperFacade.map(distributionUserWalletBillDTO, DistributionUserWalletBill.class);
        distributionUserWalletBillService.update(distributionUserWalletBill);
        return ServerResponseEntity.success();
    }
}
