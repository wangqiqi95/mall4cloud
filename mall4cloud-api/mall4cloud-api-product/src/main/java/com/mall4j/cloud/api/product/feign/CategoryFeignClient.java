package com.mall4j.cloud.api.product.feign;

import com.mall4j.cloud.api.product.dto.IphSyncCategoryDto;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.product.vo.CategoryVO;
import com.mall4j.cloud.common.product.vo.app.CategoryAppVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
/**
 * @author lhd
 * @date 2020/12/23
 */
@FeignClient(value = "mall4cloud-product",contextId = "category")
public interface CategoryFeignClient {

    /**
     * 获取所有一级分类信息
     * @return 一级分类信息
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/category/listByOneLevel")
    ServerResponseEntity<List<CategoryAppVO>> listByOneLevel();

    /**
     * 根据上级id，获取子分类id列表
     * @param categoryId
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/category/listCategoryId")
    ServerResponseEntity<List<Long>> listCategoryId(@RequestParam("categoryId") Long categoryId);

    /**
     * 根据分类id，获取分类及分类国际化信息
     * @param categoryId
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/category/getByCategoryId")
    ServerResponseEntity<CategoryVO>  getByCategoryId(@RequestParam("categoryId") Long categoryId);

    /**
     * 同步爱铺货分类信息
     * @param iphSyncCategoryDto
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/category/getByCategoryId")
    ServerResponseEntity<Long> syncCategoryByIPH(@RequestBody IphSyncCategoryDto iphSyncCategoryDto);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/category/listByParentId")
    ServerResponseEntity<List<CategoryVO>> listByParentId(@RequestParam("parentId") Long parentId, @RequestParam(value = "shopId", defaultValue = "0") Long shopId);
}
