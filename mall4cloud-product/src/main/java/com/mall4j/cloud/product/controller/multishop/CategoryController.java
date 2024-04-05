package com.mall4j.cloud.product.controller.multishop;

import com.mall4j.cloud.api.product.dto.CategoryShopDTO;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.i18n.I18nMessage;
import com.mall4j.cloud.common.product.vo.app.CategoryAppVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.product.service.CategoryService;
import com.mall4j.cloud.product.service.CategoryShopService;
import com.mall4j.cloud.product.vo.CategoryShopVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author lth
 */
@RestController("multishopCategoryController")
@RequestMapping("/m/apply_shop/category")
@Api(tags = "multishop-分类信息")
public class CategoryController {

    @Autowired
    private CategoryShopService categoryShopService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/signing")
    @ApiOperation(value = "签约分类", notes = "签约分类")
    public ServerResponseEntity<Void> signingCategory(@Valid @RequestBody List<CategoryShopDTO> categoryShopDTOList) {
        Long shopId = AuthUserContext.get().getTenantId();
        categoryShopService.signingCategory(categoryShopDTOList, shopId);
        return ServerResponseEntity.success();
    }

    @GetMapping("/list_by_shop")
    @ApiOperation(value = "获取签约的分类列表(返回所有)", notes = "获取签约的分类列表(返回所有)")
    public ServerResponseEntity<List<CategoryShopVO>> listByShopId() {
        Long shopId = AuthUserContext.get().getTenantId();
        List<CategoryShopVO> categoryShopVOList = categoryShopService.listByShopId(shopId, I18nMessage.getLang());
        return ServerResponseEntity.success(categoryShopVOList);
    }

    @GetMapping("/platform_categories")
    @ApiOperation(value = "获取平台所有的分类信息", notes = "获取所有的分类列表信息")
    public ServerResponseEntity<List<CategoryAppVO>> platformCategories(@RequestParam(value = "shopId", defaultValue = "0", required = false) Long shopId) {
        if (shopId!= Constant.PLATFORM_SHOP_ID){
            shopId = Constant.MAIN_SHOP;
        }
        return ServerResponseEntity.success(categoryService.platformCategories(shopId));
    }

    @GetMapping("/list_signing_category")
    @ApiModelProperty(value = "获取签约的可用分类列表(发布商品时使用,包含对应的上级分类）", notes = "获取签约的可用分类列表(发布商品时使用,包含对应的上级分类）")
    public ServerResponseEntity<List<CategoryAppVO>> listSigningCategory() {
        return ServerResponseEntity.success(categoryService.listSigningCategory(AuthUserContext.get().getTenantId(), I18nMessage.getLang()));
    }

    @DeleteMapping("/delete_signing_category")
    @ApiOperation(value = "根据店铺id与分类id删除签约信息", notes = "根据店铺id与分类id删除签约信息")
    public ServerResponseEntity<Void> deleteSigningCategory(@RequestParam("shopId") Long shopId, @RequestParam("categoryId") Long categoryId) {
        categoryShopService.deleteSigningCategory(shopId, categoryId);
        return ServerResponseEntity.success();
    }

}
