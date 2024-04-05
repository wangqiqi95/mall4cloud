package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.distribution.model.DistributionMomentsProduct;
import com.mall4j.cloud.distribution.service.DistributionMomentsProductService;
import com.mall4j.cloud.distribution.vo.DistributionMomentsProductVO;
import com.mall4j.cloud.distribution.dto.DistributionMomentsProductDTO;

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
import javax.validation.Valid;

/**
 * 分销推广-朋友圈商品
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:57
 */
@RestController("platformDistributionMomentsProductController")
@RequestMapping("/p/distribution_moments_product")
@Api(tags = "平台端-分销推广-朋友圈商品")
public class DistributionMomentsProductController {

    @Autowired
    private DistributionMomentsProductService distributionMomentsProductService;

    @Autowired
	private MapperFacade mapperFacade;

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

    @PostMapping
    @ApiOperation(value = "保存分销推广-朋友圈商品", notes = "保存分销推广-朋友圈商品")
    public ServerResponseEntity<Void> save(@Valid @RequestBody DistributionMomentsProductDTO distributionMomentsProductDTO) {
        DistributionMomentsProduct distributionMomentsProduct = mapperFacade.map(distributionMomentsProductDTO, DistributionMomentsProduct.class);
        distributionMomentsProduct.setId(null);
        distributionMomentsProductService.save(distributionMomentsProduct);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新分销推广-朋友圈商品", notes = "更新分销推广-朋友圈商品")
    public ServerResponseEntity<Void> update(@Valid @RequestBody DistributionMomentsProductDTO distributionMomentsProductDTO) {
        DistributionMomentsProduct distributionMomentsProduct = mapperFacade.map(distributionMomentsProductDTO, DistributionMomentsProduct.class);
        distributionMomentsProductService.update(distributionMomentsProduct);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除分销推广-朋友圈商品", notes = "根据分销推广-朋友圈商品id删除分销推广-朋友圈商品")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        distributionMomentsProductService.deleteById(id);
        return ServerResponseEntity.success();
    }
}
