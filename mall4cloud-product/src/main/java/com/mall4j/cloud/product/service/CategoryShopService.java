package com.mall4j.cloud.product.service;

import com.mall4j.cloud.product.bo.CategoryRateBO;
import com.mall4j.cloud.api.product.dto.CategoryShopDTO;
import com.mall4j.cloud.product.vo.CategoryShopVO;

import java.util.List;

/**
 * 店铺签约分类关联信息
 *
 * @Author lth
 * @Date 2021/4/25 14:33
 */
public interface CategoryShopService {
    /**
     * 签约平台分类
     * @param categoryShopDTOList
     * @param shopId
     */
    void signingCategory(List<CategoryShopDTO> categoryShopDTOList, Long shopId);

    /**
     * 根据店铺id获取签约分类列表
     * @param shopId
     * @param lang
     * @return
     */
    List<CategoryShopVO> listByShopId(Long shopId, Integer lang);

    /**
     * 根据店铺id获取平台佣金
     * @param shopId 店铺id
     * @return 平台佣金
     */
    List<CategoryRateBO> listRateByShopId(Long shopId);

    /**
     * 根据分类id删除店铺分类关联关系
     * @param categoryId
     */
    void deleteByCategoryId(Long categoryId);

    /**
     * 根据存在变化的分类的id清除缓存
     * @param categoryId
     */
    void removeCacheByChangeCategoryId(Long categoryId);

    /**
     * 根据店铺id批量保存分类签约信息
     * @param categoryShopDTOList
     * @param shopId
     */
    void insertBatchByShopId(List<CategoryShopDTO> categoryShopDTOList, Long shopId);

    /**
     * 根据存在变化的分类的id列表清除缓存
     * @param categoryIds
     */
    void removeCacheByChangeCategoryIds(List<Long> categoryIds);

    /**
     * 根据店铺id与分类id查询签约关系信息
     * @param shopId
     * @param categoryId
     * @return
     */
    CategoryShopVO getByShopIdAndCategoryId(Long shopId, Long categoryId);

    /**
     * 根据店铺id与分类id统计签约数量
     * @param shopId
     * @param categoryId
     * @return
     */
    int countByShopIdAndCategoryId(Long shopId, Long categoryId);

    /**
     * 根据店铺id与分类id删除签约关系
     * @param shopId
     * @param categoryId
     */
    void deleteSigningCategory(Long shopId, Long categoryId);
}
