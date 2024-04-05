package com.mall4j.cloud.product.mapper;

import com.mall4j.cloud.common.product.vo.CategoryVO;
import com.mall4j.cloud.product.model.CategoryBrand;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 品牌分类关联信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
public interface CategoryBrandMapper {

	/**
	 * 根据品牌分类关联信息id删除品牌分类关联信息
	 *
	 * @param brandId
	 */
	void deleteByBrandId(@Param("brandId") Long brandId);

	/**
	 * 批量保存
	 *
	 * @param categoryBrandList
	 */
	void saveBatch(@Param("categoryBrandList") List<CategoryBrand> categoryBrandList);

	/**
	 * 根据品牌id获取关联的分类id
	 *
	 * @param brandId 品牌id
	 * @return 分类id列表
	 */
	List<Long> getCategoryIdsByBrandId(@Param("brandId") Long brandId);

	/**
	 * 根据品牌id和分类id列表删除关联信息
	 *
	 * @param brandId
	 * @param categoryIds
	 */
	void deleteByBrandIdAndCategoryIds(@Param("brandId") Long brandId, @Param("categoryIds") List<Long> categoryIds);

	/**
	 * 获取品牌分类信息
	 * @param brandId 品牌id
	 * @param lang 语言
	 * @return 分类信息
	 */
    List<CategoryVO> getCategoryByBrandId(@Param("brandId") Long brandId, @Param("lang") Integer lang);

	/**
	 * 根据品牌id与分类id统计数量
	 * @param brandId
	 * @param categoryId
	 * @return
	 */
    int countByBrandIdAndCategoryId(@Param("brandId") Long brandId, @Param("categoryId") Long categoryId);
}
