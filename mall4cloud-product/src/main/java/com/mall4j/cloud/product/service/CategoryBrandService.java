package com.mall4j.cloud.product.service;

import com.mall4j.cloud.common.product.vo.CategoryVO;

import java.util.List;

/**
 * 品牌分类关联信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
public interface CategoryBrandService {

	/**
	 * 根据品牌id删除品牌分类关联信息
	 * @param brandId
	 */
	void deleteByBrandId(Long brandId);

	/**
	 * 保存品牌信息
	 * @param brandId
	 * @param categoryIds
	 */
    void saveByCategoryIds(Long brandId, List<Long> categoryIds);

	/**
	 * 更新品牌信息
	 * @param brandId
	 * @param categoryIds
	 */
	void updateByCategoryIds(Long brandId, List<Long> categoryIds);

	/**
	 * 根据品牌id或者关联的分类列表
	 * @param brandId
	 * @return
	 */
	List<Long> getCategoryIdBrandId(Long brandId);

	/**
	 * 获取品牌绑定的分类信息
	 * @param brandId 品牌id
	 * @return 分类列表
	 */
	List<CategoryVO> getCategoryByBrandId(Long brandId);

	/**
	 * 根据品牌id与分类id统计数量
	 * @param brandId
	 * @param categoryId
	 * @return
	 */
    int countByBrandIdAndCategoryId(Long brandId, Long categoryId);
}
