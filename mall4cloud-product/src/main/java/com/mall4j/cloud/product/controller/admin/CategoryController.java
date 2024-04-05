package com.mall4j.cloud.product.controller.admin;

import com.mall4j.cloud.api.product.constant.CategoryLevel;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.i18n.I18nMessage;
import com.mall4j.cloud.common.product.vo.CategoryVO;
import com.mall4j.cloud.common.product.vo.app.CategoryAppVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.product.dto.CategoryDTO;
import com.mall4j.cloud.product.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 分类信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
@RestController("adminCategoryController")
@RequestMapping("/mp/category")
@Api(tags = "admin-分类信息")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private MapperFacade mapperFacade;

    @GetMapping
    @ApiOperation(value = "获取分类信息", notes = "根据categoryId获取分类信息")
    public ServerResponseEntity<CategoryVO> getByCategoryId(@RequestParam Long categoryId) {
        return ServerResponseEntity.success(categoryService.getInfo(categoryId));
    }

    @PostMapping
    @ApiOperation(value = "保存分类信息", notes = "保存分类信息")
    public ServerResponseEntity<Void> save(@RequestParam(value = "storeId", required = false) Long storeId, @Valid @RequestBody CategoryDTO categoryDTO) {
        if (categoryDTO.getShopId()!= Constant.PLATFORM_SHOP_ID){
            categoryDTO.setShopId(Constant.MAIN_SHOP);
        }
        categoryService.save(categoryDTO);
        categoryService.removeCategoryCache(Objects.nonNull(storeId)?storeId:categoryDTO.getShopId(), null);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新分类信息", notes = "更新分类信息")
    public ServerResponseEntity<Void> update(@Valid @RequestBody CategoryDTO categoryDTO) {
        if (categoryDTO.getShopId()!= Constant.PLATFORM_SHOP_ID){
            categoryDTO.setShopId(Constant.MAIN_SHOP);
        }
        categoryService.update(categoryDTO);
        categoryService.removeCategoryCache(categoryDTO.getShopId(), categoryDTO.getCategoryId());
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除分类信息", notes = "根据分类信息id删除分类信息")
    public ServerResponseEntity<Void> delete(@RequestParam(value = "shopId", required = false) Long shopId, @RequestParam Long categoryId) {
        CategoryVO category = categoryService.getById(categoryId);
        if (Objects.isNull(category)) {
            throw new LuckException(ResponseEnum.DATA_ERROR);
        }
        categoryService.deleteById(categoryId, shopId);
        categoryService.removeCategoryCache(category.getShopId(), categoryId);
        return ServerResponseEntity.success();
    }

    @GetMapping("/platform_categories")
    @ApiOperation(value = "获取平台所有的分类信息", notes = "获取所有的分类列表信息")
    public ServerResponseEntity<List<CategoryAppVO>> platformCategories(@RequestParam("shopId") Long shopId) {
        if (shopId!= Constant.PLATFORM_SHOP_ID){
            shopId = Constant.MAIN_SHOP;
        }
        return ServerResponseEntity.success(categoryService.platformCategories(shopId));
    }

    @GetMapping("/shop_categories")
    @ApiOperation(value = "获取店铺所有的分类信息", notes = "获取店铺所有的分类信息")
    public ServerResponseEntity<List<CategoryAppVO>> shopCategories(@RequestParam(value = "storeId") Long storeId) {
        return ServerResponseEntity.success(categoryService.list(storeId));
    }

    @GetMapping("/enable_categories")
    @ApiOperation(value = "获取平台/店铺启用的分类信息", notes = "仅获取启用的分类，以及包含三级分类的分类信息")
    public ServerResponseEntity<List<CategoryAppVO>> enableCategories(@RequestParam(value = "shopId",defaultValue = "0",required = false) Long shopId) {
        //查询总店所有分类作为店铺分类
        if (shopId!=Constant.PLATFORM_SHOP_ID){
            shopId = Constant.MAIN_SHOP;
        }
        List<CategoryAppVO> categories = categoryService.shopCategoryList(shopId);
        List<CategoryAppVO> categoryList = new ArrayList<>();
        for (CategoryAppVO primaryCategoryVO : categories) {
            categoryList.add(primaryCategoryVO);
            categoryList.addAll(primaryCategoryVO.getCategories());
            for (CategoryAppVO secondaryCategory : primaryCategoryVO.getCategories()) {
//                categoryList.add(secondaryCategory);
                categoryList.addAll(secondaryCategory.getCategories());
            }

        }
        return ServerResponseEntity.success(categoryList);
    }

    @GetMapping("/get_list_by_parent_id")
    @ApiOperation(value = "根据上级id，获取分类列表信息", notes = "根据上级id，获取分类列表信息")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "parentId", value = "父类id")
    })
    public ServerResponseEntity<List<CategoryAppVO>> getListByParentId(@RequestParam(value = "parentId") Long parentId) {
        return ServerResponseEntity.success(categoryService.listByShopIdAndParenId(parentId, AuthUserContext.get().getTenantId(), I18nMessage.getLang()));
    }

    @PutMapping(value = "/category_enable_or_disable")
    @ApiOperation(value = "分类的启用或禁用", notes = "分类的启用或禁用")
    public ServerResponseEntity<Boolean> categoryEnableOrDisable(@RequestBody CategoryDTO categoryDTO) {
        categoryService.categoryEnableOrDisable(categoryDTO);
        // 批量更新
        categoryService.removeCategoryCache(categoryDTO.getShopId(), categoryDTO.getCategoryId());
        return ServerResponseEntity.success();
    }



    @PutMapping(value = "/shop/batch")
    @ApiOperation(value = "批量更新店铺分类", notes = "批量更新店铺分类")
    public ServerResponseEntity<Boolean> categoryShopUpdateBatch(@RequestBody List<CategoryDTO> categoryDTOList) {
        categoryService.categoryShopUpdateBatch(categoryDTOList);
        return ServerResponseEntity.success();
    }



}
