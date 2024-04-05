package com.mall4j.cloud.product.controller.app;

import cn.hutool.core.util.ArrayUtil;
import com.mall4j.cloud.api.product.constant.CategoryLevel;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.i18n.I18nMessage;
import com.mall4j.cloud.common.product.vo.CategoryVO;
import com.mall4j.cloud.common.product.vo.app.CategoryAppVO;
import com.mall4j.cloud.product.service.CategoryService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.product.vo.app.CategorySearchAppVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * 分类信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
@RestController("appCategoryController")
@RequestMapping("/ua/category")
@Api(tags = "app-分类信息")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private MapperFacade mapperFacade;

    @GetMapping("/category_list")
    @ApiOperation(value = "获取指定分类下的分类列表（顶级分类的parentId为0,默认为一级分类）", notes = "获取指定分类下的分类列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId", value = "分类ID", dataType = "Long"),
            @ApiImplicitParam(name = "shopId", value = "店铺id", dataType = "Long")
    })
    public ServerResponseEntity<List<CategoryAppVO>> categoryList(@RequestParam(value = "parentId", defaultValue = "0") Long parentId, @RequestParam(value = "shopId", defaultValue = "0") Long shopId) {
        List<CategoryAppVO> categories = categoryService.listByShopIdAndParenId(shopId,parentId, I18nMessage.getLang());

        return ServerResponseEntity.success(mapperFacade.mapAsList(categories, CategoryAppVO.class));
    }

    @GetMapping("/shop_category_list")
    @ApiOperation(value = "店铺/平台的全部分类列表接口", notes = "店铺/平台分类列表接口")
    @ApiImplicitParam(name = "shopId", value = "店铺id", dataType = "Long")
    public ServerResponseEntity<List<CategoryAppVO>> shopCategoryList(@RequestParam(value = "shopId", defaultValue = "1",required = false) Long shopId) {
        List<CategoryAppVO> categories = categoryService.shopCategoryList(shopId);
        return ServerResponseEntity.success(categories);
    }


    @GetMapping("/category_search_item")
    @ApiOperation(value = "分类-搜索参数", notes = "分类-搜索参数")
    @ApiImplicitParam(name = "categoryId", value = "分类id", required = true, dataType = "Long")
    public ServerResponseEntity<CategorySearchAppVO> categorySearchItem(@RequestParam(value = "categoryId") Long categoryId) {
        CategoryVO category = categoryService.getById(categoryId);
        Integer lang = I18nMessage.getLang();
        if (Objects.isNull(category)) {
            return ServerResponseEntity.success();
        }
        CategorySearchAppVO categorySearch = new CategorySearchAppVO();
        // 查询一级分类
        if (Objects.equals(category.getLevel(), CategoryLevel.First.value())) {
            categorySearch.setPrimaryCategory(mapperFacade.map(category, CategoryAppVO.class));
        }
        // 查询二级分类
        else if (Objects.equals(category.getLevel(), CategoryLevel.SECOND.value())) {
            List<CategoryAppVO> categoryAppVOList = categoryService.listByShopIdAndParenId(category.getShopId(), category.getParentId(), lang);
            for (CategoryAppVO categoryAppVO : categoryAppVOList) {
                if (Objects.equals(categoryAppVO.getCategoryId(), category.getParentId())) {
                    categorySearch.setPrimaryCategory(categoryAppVO);
                }
            }
            categorySearch.setSecondaryCategory(categoryService.listByShopIdAndParenId(category.getShopId(), category.getParentId(), lang));
        }
        // 查询三级分类
        else {
            categorySearch.setCategoryVO(categoryService.listByShopIdAndParenId(category.getShopId(), category.getParentId(), lang));
            String[] split = category.getPath().split(Constant.CATEGORY_INTERVAL);
            if (ArrayUtil.isNotEmpty(split)) {
                Long parentId = Long.valueOf(split[0]);
                List<CategoryAppVO> categoryApps = categoryService.listByShopIdAndParenId(category.getShopId(), Constant.DEFAULT_ID, lang);
                for (CategoryAppVO categoryAppVO : categoryApps) {
                    if (Objects.equals(categoryAppVO.getCategoryId(), parentId)) {
                        categorySearch.setPrimaryCategory(categoryAppVO);
                    }
                }
                categorySearch.setSecondaryCategory(categoryService.listByShopIdAndParenId(category.getShopId(), Long.valueOf(split[0]), lang));
            }
        }
        return ServerResponseEntity.success(categorySearch);
    }
}
