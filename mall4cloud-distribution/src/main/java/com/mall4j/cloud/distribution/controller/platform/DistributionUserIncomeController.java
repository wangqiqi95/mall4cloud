package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.dto.DistributionUserIncomeDTO;
import com.mall4j.cloud.distribution.model.DistributionUserIncome;
import com.mall4j.cloud.distribution.service.DistributionUserIncomeService;
import com.mall4j.cloud.distribution.vo.DistributionUserIncomeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 分销收入记录信息
 *
 * @author cl
 * @date 2021-08-09 14:14:09
 */
@RestController("platformDistributionUserIncomeController")
@RequestMapping("/p/distribution_user_income")
@Api(tags = "分销收入记录信息")
public class DistributionUserIncomeController {

    @Autowired
    private DistributionUserIncomeService distributionUserIncomeService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取分销收入记录信息列表", notes = "分页获取分销收入记录信息列表")
	public ServerResponseEntity<PageVO<DistributionUserIncome>> page(@Valid PageDTO pageDTO) {
		PageVO<DistributionUserIncome> distributionUserIncomePage = distributionUserIncomeService.page(pageDTO);
		return ServerResponseEntity.success(distributionUserIncomePage);
	}

    @GetMapping("/effect_page")
    @ApiOperation(value = "获取分销推广效果列表", notes = "分页获取分销推广效果列表")
    public ServerResponseEntity<PageVO<DistributionUserIncomeVO>> effectPage(@Valid PageDTO pageDTO, DistributionUserIncomeDTO distributionUserIncomeDTO, String userMobile) {
        PageVO<DistributionUserIncomeVO> distributionUserIncomePage = distributionUserIncomeService.effectPage(pageDTO, distributionUserIncomeDTO, userMobile, Constant.PLATFORM_SHOP_ID);
        return ServerResponseEntity.success(distributionUserIncomePage);
    }

	@GetMapping
    @ApiOperation(value = "获取分销收入记录信息", notes = "根据incomeId获取分销收入记录信息")
    public ServerResponseEntity<DistributionUserIncome> getByIncomeId(@RequestParam Long incomeId) {
        return ServerResponseEntity.success(distributionUserIncomeService.getByIncomeId(incomeId));
    }


}
