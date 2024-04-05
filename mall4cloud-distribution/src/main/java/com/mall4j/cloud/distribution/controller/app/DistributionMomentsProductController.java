package com.mall4j.cloud.distribution.controller.app;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.model.DistributionMomentsProduct;
import com.mall4j.cloud.distribution.service.DistributionMomentsProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 分销推广-朋友圈商品
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:57
 */
@RestController("appDistributionMomentsProductController")
@RequestMapping("/distribution_moments_product")
@Api(tags = "分销推广-朋友圈商品")
public class DistributionMomentsProductController {

    @Autowired
    private DistributionMomentsProductService distributionMomentsProductService;


	@GetMapping("/page")
	@ApiOperation(value = "获取分销推广-朋友圈商品列表", notes = "分页获取分销推广-朋友圈商品列表")
	public ServerResponseEntity<PageVO<DistributionMomentsProduct>> page(@Valid PageDTO pageDTO) {
		PageVO<DistributionMomentsProduct> distributionMomentsProductPage = distributionMomentsProductService.page(pageDTO);
		return ServerResponseEntity.success(distributionMomentsProductPage);
	}

	@GetMapping
    @ApiOperation(value = "获取分销推广-朋友圈商品", notes = "根据id获取分销推广-朋友圈商品")
    public ServerResponseEntity<DistributionMomentsProduct> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(distributionMomentsProductService.getById(id));
    }

}
