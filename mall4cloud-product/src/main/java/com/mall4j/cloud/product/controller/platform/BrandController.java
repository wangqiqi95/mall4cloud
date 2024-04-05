package com.mall4j.cloud.product.controller.platform;

import cn.hutool.core.collection.CollUtil;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.product.vo.BrandVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.product.dto.BrandDTO;
import com.mall4j.cloud.product.dto.BrandSigningDTO;
import com.mall4j.cloud.product.service.BrandService;
import com.mall4j.cloud.product.service.BrandShopService;
import com.mall4j.cloud.product.service.CategoryBrandService;
import com.mall4j.cloud.product.service.CategoryService;
import com.mall4j.cloud.product.vo.BrandSigningVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 品牌信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
@RestController("platformBrandController")
@RequestMapping("/p/brand")
@Api(tags = "platform-品牌信息")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryBrandService categoryBrandService;

    @Autowired
    private BrandShopService brandShopService;

    @GetMapping("/ua/dbMasterTest")
    @ApiOperation(value = "dbMasterTest", notes = "dbMasterTest")
    public ServerResponseEntity<String> dbMasterTest() {
        brandService.dbMasterTest();
        return ServerResponseEntity.success();
    }
    @GetMapping("/ua/dbSlaveTest")
    @ApiOperation(value = "dbSlaveTest", notes = "dbSlaveTest")
    public ServerResponseEntity<Void> dbSlaveTest() {
        brandService.dbSlaveTest();
        return ServerResponseEntity.success();
    }

    @GetMapping("/page")
    @ApiOperation(value = "获取品牌信息列表", notes = "分页获取品牌信息列表")
    public ServerResponseEntity<PageVO<BrandVO>> page(@Valid PageDTO pageDTO, BrandDTO brandDTO) {
        brandDTO.setShopId(Constant.PLATFORM_SHOP_ID);
        PageVO<BrandVO> brandPage = brandService.page(pageDTO, brandDTO);
        return ServerResponseEntity.success(brandPage);
    }

    @GetMapping
    @ApiOperation(value = "获取品牌信息", notes = "根据brandId获取品牌信息")
    public ServerResponseEntity<BrandVO> getByBrandId(@RequestParam Long brandId) {
        BrandVO brand = brandService.getInfo(brandId);
        categoryService.getPathNames(brand.getCategories());
        return ServerResponseEntity.success(brand);
    }

    @PostMapping
    @ApiOperation(value = "保存品牌信息", notes = "保存品牌信息")
    public ServerResponseEntity<Void> save(@Valid @RequestBody BrandDTO brandDTO) {
        brandDTO.setShopId(Constant.PLATFORM_SHOP_ID);
        brandService.save(brandDTO);
        brandService.removeCache(brandDTO.getCategoryIds());
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新品牌信息", notes = "更新品牌信息")
    public ServerResponseEntity<Void> update(@Valid @RequestBody BrandDTO brandDTO) {
        brandService.update(brandDTO);
        // 清除缓存
        List<Long> categoryIds = categoryBrandService.getCategoryIdBrandId(brandDTO.getBrandId());
        if (CollUtil.isNotEmpty(brandDTO.getCategoryIds())) {
            categoryIds.addAll(brandDTO.getCategoryIds());
        }
        brandService.removeCache(categoryIds);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除品牌信息", notes = "根据品牌信息id删除品牌信息")
    public ServerResponseEntity<Void> delete(@RequestParam Long brandId) {
        brandService.deleteById(brandId);
        brandService.removeCache(categoryBrandService.getCategoryIdBrandId(brandId));
        return ServerResponseEntity.success();
    }

    @PutMapping(value = "/update_brand_status")
    @ApiOperation(value = "更新品牌状态（启用或禁用）", notes = "更新品牌状态（启用或禁用）")
    public ServerResponseEntity<Void> updateBrandStatus(@RequestBody BrandDTO brandDTO) {
        if (Objects.isNull(brandDTO.getStatus())) {
            throw new LuckException("状态不能为空");
        }
        if (Objects.isNull(brandDTO.getBrandId())) {
            throw new LuckException("品牌id不能为空");
        }
        brandService.updateBrandStatus(brandDTO);
        // 清除缓存
        List<Long> categoryIds = categoryBrandService.getCategoryIdBrandId(brandDTO.getBrandId());
        // 获取当前节点所有父节点的分类ids，以及当前分类节点的父级节点的父级几点的分类ids
        List<Long> parentCategoryIds = categoryService.getParentIdsByCategoryId(categoryIds);
        if (CollUtil.isNotEmpty(parentCategoryIds)) {
            categoryIds.addAll(parentCategoryIds);
        }
        brandService.removeCache(CollUtil.isNotEmpty(categoryIds) ? categoryIds : new ArrayList<>());
        return ServerResponseEntity.success();
    }

    @GetMapping("/signing_info_by_shop_id")
    @ApiOperation(value = "根据店铺id获取签约的品牌列表", notes = "根据店铺id获取签约的品牌列表")
    public ServerResponseEntity<BrandSigningVO> signingInfoByShopId(@RequestParam Long shopId) {
        BrandSigningVO brandSigningVO = brandShopService.listSigningByShopId(shopId);
        return ServerResponseEntity.success(brandSigningVO);
    }

    @PutMapping("/signing")
    @ApiOperation(value = "根据店铺id更新店铺下的签约品牌", notes = "根据店铺id更新店铺下的签约品牌")
    public ServerResponseEntity<Void> signingBrands(@RequestBody BrandSigningDTO brandSigningDTO, @RequestParam(value = "shopId") Long shopId) {
        brandShopService.signingBrands(brandSigningDTO, shopId);
        return ServerResponseEntity.success();
    }

}
