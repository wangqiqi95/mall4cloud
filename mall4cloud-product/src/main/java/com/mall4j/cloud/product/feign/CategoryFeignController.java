package com.mall4j.cloud.product.feign;

import com.mall4j.cloud.api.product.dto.IphSyncCategoryDto;
import com.mall4j.cloud.api.product.feign.CategoryFeignClient;
import com.mall4j.cloud.common.i18n.I18nMessage;
import com.mall4j.cloud.common.product.vo.CategoryVO;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.product.vo.app.CategoryAppVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lhd
 * @date 2020/12/23
 */
@RestController
public class CategoryFeignController implements CategoryFeignClient {

    @Autowired
    private CategoryService categoryService;

    @Override
    public ServerResponseEntity<List<CategoryAppVO>> listByOneLevel() {
        return ServerResponseEntity.success(categoryService.listByShopIdAndParenId(Constant.PLATFORM_SHOP_ID, Constant.CATEGORY_ID, I18nMessage.getLang()));
    }

    @Override
    public ServerResponseEntity<List<Long>> listCategoryId(Long categoryId) {
        CategoryVO category = categoryService.getById(categoryId);
        List<Long> categoryIds = categoryService.listCategoryId(category.getShopId(), category.getParentId());
        return ServerResponseEntity.success(categoryIds);
    }

    @Override
    public ServerResponseEntity<CategoryVO> getByCategoryId(Long categoryId) {
        return ServerResponseEntity.success(categoryService.getById(categoryId));
    }

    /**
     * 同步爱铺货分类信息
     *
     * @param iphSyncCategoryDto
     * @return
     */
    @Override
    public ServerResponseEntity<Long> syncCategoryByIPH(IphSyncCategoryDto iphSyncCategoryDto) {
        Long categoryId = categoryService.sycnIPH(iphSyncCategoryDto);
        return ServerResponseEntity.success(categoryId);
    }

    @Override
    public ServerResponseEntity<List<CategoryVO>> listByParentId(Long parentId, Long shopId) {
        List<CategoryVO> categoryVOList =  categoryService.listbyParntId(parentId, shopId);
        return ServerResponseEntity.success(categoryVOList);
    }
}
