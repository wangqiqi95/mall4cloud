package com.mall4j.cloud.product.controller.multishop;

import com.mall4j.cloud.common.constant.IsPassShopEnum;
import com.mall4j.cloud.common.product.vo.BrandVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.product.dto.BrandSigningDTO;
import com.mall4j.cloud.product.service.BrandService;
import com.mall4j.cloud.product.service.BrandShopService;
import com.mall4j.cloud.product.vo.BrandSigningVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author lth
 * @Date 2021/4/30 13:26
 */
@RestController("multishopBrandController")
@RequestMapping("/m/apply_shop/brand")
@Api(tags = "multishop-品牌信息")
public class BrandController {

    @Autowired
    private BrandShopService brandShopService;

    @Autowired
    private BrandService brandService;

    @PostMapping("/signing")
    @ApiOperation(value = "签约品牌", notes = "签约品牌")
    public ServerResponseEntity<Void> signingBrands(@Valid @RequestBody BrandSigningDTO brandSigningDTO) {
        Long shopId = AuthUserContext.get().getTenantId();
        if (Objects.equals(AuthUserContext.get().getIsPassShop(), IsPassShopEnum.YES.value())) {
            brandSigningDTO.setCustomizeBrandList(new ArrayList<>());
        }
        brandShopService.signingBrands(brandSigningDTO, shopId);
        return ServerResponseEntity.success();
    }

    @GetMapping("/list_signing")
    @ApiOperation(value = "获取店铺下已签约的品牌列表", notes = "获取店铺下已签约的品牌列表")
    public ServerResponseEntity<BrandSigningVO> listSigning() {
        Long shopId = AuthUserContext.get().getTenantId();
        BrandSigningVO brandSigningVO = brandShopService.listSigningByShopId(shopId);
        return ServerResponseEntity.success(brandSigningVO);
    }

    @GetMapping("/list_available_by_category_and_name")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "categoryId",value = "分类id"),
            @ApiImplicitParam(name = "brandName",value = "品牌名称")
    })
    @ApiOperation(value = "根据分类id与品牌名称获取分类下的品牌与店铺签约的品牌", notes = "根据分类id与品牌名称获取分类下的品牌与店铺签约的品牌")
    public ServerResponseEntity<List<BrandVO>> listAvailableBrandByCategoryIdAndBrandName(@RequestParam Long categoryId, @RequestParam(defaultValue = "") String brandName) {
        return ServerResponseEntity.success(brandService.listAvailableBrandByCategoryIdAndBrandNameAndShopId(categoryId, brandName, AuthUserContext.get().getTenantId()));
    }


}
