package com.mall4j.cloud.product.mapper;

import com.mall4j.cloud.product.bo.CategoryRateBO;
import com.mall4j.cloud.api.product.dto.CategoryShopDTO;
import com.mall4j.cloud.product.model.CategoryShop;
import com.mall4j.cloud.product.vo.CategoryShopVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 店铺签约分类关联信息
 *
 * @Author lth
 * @Date 2021/4/25 14:37
 */
public interface CategoryShopMapper {
    /**
     * 根据店铺id删除所有以签约的信息
     * @param shopId
     */
    void deleteByShopId(@Param("shopId") Long shopId);
    /**
     * 批量保存分类签约信息
     * @param categoryShopDTOList
     * @param shopId
     */
    void saveBatch(@Param("categoryShopList") List<CategoryShopDTO> categoryShopDTOList, @Param("shopId") Long shopId);

    /**
     * 根据店铺id获取签约分类信息列表
     * @param shopId
     * @param lang
     * @return
     */
    List<CategoryShopVO> listByShopId(@Param("shopId") Long shopId, @Param("lang") Integer lang);

    /**
     * 获取店铺分类关联信息列表
     * @return 品牌分类关联信息列表
     */
    List<CategoryShop> list();

    /**
     * 根据店铺分类关联信息id获取店铺分类关联信息
     *
     * @param categoryShopId 店铺分类关联信息id
     * @return 店铺分类关联信息
     */
    CategoryShop getByCategoryShopId(@Param("categoryShopId") Long categoryShopId);

    /**
     * 保存店铺分类关联信息
     * @param categoryShop 店铺分类关联信息
     */
    void save(@Param("categoryShop") CategoryShop categoryShop);

    /**
     * 更新店铺分类关联信息
     * @param categoryShop 店铺分类关联信息
     */
    void update(@Param("categoryShop") CategoryShop categoryShop);

    /**
     * 根据店铺分类关联信息id删除店铺分类关联信息
     * @param categoryShopId
     */
    void deleteById(@Param("categoryShopId") Long categoryShopId);

    /**
     * 根据店铺id获取平台佣金
     * @param shopId 店铺id
     * @return 平台佣金
     */
    List<CategoryRateBO> listRateByShopId(@Param("shopId") Long shopId);

    /**
     * 根据分类id删除店铺分类关联关系
     * @param categoryId
     */
    void deleteByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * 根据分类id获取签约了该分类的店铺id列表
     * @param categoryId
     * @return
     */
    List<Long> listShopIdByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * 根据分类id列表获取店铺id列表
     * @param categoryIds
     * @return
     */
    List<Long> listShopIdByCategoryIds(@Param("categoryIds") List<Long> categoryIds);

    /**
     * 根据店铺id与分类id查询签约关系
     * @param shopId
     * @param categoryId
     * @return
     */
    CategoryShopVO getByShopIdAndCategoryId(@Param("shopId") Long shopId, @Param("categoryId") Long categoryId);

    /**
     * 根据店铺id与分类id统计签约数量
     * @param shopId
     * @param categoryId
     * @return
     */
    int countByShopIdAndCategoryId(@Param("shopId") Long shopId, @Param("categoryId") Long categoryId);

    /**
     * 根据店铺id与分类id删除签约信息
     * @param shopId
     * @param categoryId
     */
    void deleteByShopIdAndCategoryId(@Param("shopId") Long shopId, @Param("categoryId") Long categoryId);
}
