package com.mall4j.cloud.distribution.controller.platform;

import com.mall4j.cloud.distribution.model.DistributionSubjectProduct;
import com.mall4j.cloud.distribution.service.DistributionSubjectProductService;
import com.mall4j.cloud.distribution.vo.DistributionSubjectProductVO;
import com.mall4j.cloud.distribution.dto.DistributionSubjectProductDTO;

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
 * 分销推广-专题门店
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:57
 */
@RestController("platformDistributionSubjectProductController")
@RequestMapping("/p/distribution_subject_product")
@Api(tags = "平台端-分销推广-专题门店")
public class DistributionSubjectProductController {

    @Autowired
    private DistributionSubjectProductService distributionSubjectProductService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取分销推广-专题门店列表", notes = "分页获取分销推广-专题门店列表")
	public ServerResponseEntity<PageVO<DistributionSubjectProduct>> page(@Valid PageDTO pageDTO) {
		PageVO<DistributionSubjectProduct> distributionSubjectProductPage = distributionSubjectProductService.page(pageDTO);
		return ServerResponseEntity.success(distributionSubjectProductPage);
	}

	@GetMapping
    @ApiOperation(value = "获取分销推广-专题门店", notes = "根据id获取分销推广-专题门店")
    public ServerResponseEntity<DistributionSubjectProduct> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(distributionSubjectProductService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "保存分销推广-专题门店", notes = "保存分销推广-专题门店")
    public ServerResponseEntity<Void> save(@Valid @RequestBody DistributionSubjectProductDTO distributionSubjectProductDTO) {
        DistributionSubjectProduct distributionSubjectProduct = mapperFacade.map(distributionSubjectProductDTO, DistributionSubjectProduct.class);
        distributionSubjectProduct.setId(null);
        distributionSubjectProductService.save(distributionSubjectProduct);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新分销推广-专题门店", notes = "更新分销推广-专题门店")
    public ServerResponseEntity<Void> update(@Valid @RequestBody DistributionSubjectProductDTO distributionSubjectProductDTO) {
        DistributionSubjectProduct distributionSubjectProduct = mapperFacade.map(distributionSubjectProductDTO, DistributionSubjectProduct.class);
        distributionSubjectProductService.update(distributionSubjectProduct);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除分销推广-专题门店", notes = "根据分销推广-专题门店id删除分销推广-专题门店")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        distributionSubjectProductService.deleteById(id);
        return ServerResponseEntity.success();
    }
}
