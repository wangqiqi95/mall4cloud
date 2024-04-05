package com.mall4j.cloud.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.api.product.bo.PlatformCommissionOrderItemBO;
import com.mall4j.cloud.common.product.vo.CategoryVO;
import com.mall4j.cloud.product.bo.CategoryRateBO;
import com.mall4j.cloud.product.model.Category;
import com.mall4j.cloud.product.vo.CategoryUseNumVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 分类信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
public interface CategoryMapper extends BaseMapper<Category> {

	/**
	 * 根据分类信息id获取分类信息
	 *
	 * @param categoryId 分类信息id
	 * @return 分类信息
	 */
	CategoryVO getById(@Param("categoryId") Long categoryId);

	/**
	 * 根据分类信息id获取分类信息，不关联category_lang表
	 *
	 * @param categoryId 分类信息id
	 * @return 分类信息
	 */
	CategoryVO selectById(@Param("categoryId") Long categoryId);

	/**
	 * 保存分类信息
	 *
	 * @param category 分类信息
	 */
	void save(@Param("category") Category category);

	/**
	 * 更新分类信息
	 *
	 * @param category 分类信息
	 * @return 更新的数量
	 */
	int update(@Param("category") Category category);

	/**
	 * 根据分类信息id删除分类信息
	 *
	 * @param categoryId
	 */
	void deleteById(@Param("categoryId") Long categoryId);

	/**
	 * 获取分类被关联的数量
	 *
	 * @param categoryId
	 * @param shopId
	 * @return
	 */
	List<CategoryUseNumVO> getCategoryUseNum(@Param("categoryId") Long categoryId, @Param("shopId") Long shopId);

	/**
	 * 获取分类列表(未删除的分类--启用、未启用状态， 用于分类管理)
	 *
	 * @param level 分类等级-后面参数多了再换成类
	 * @return
	 */
	List<CategoryVO> list(@Param("shopId") Long shopId, @Param("level") Integer level);


	/**
	 * 获取分类列表(仅启用状态的分类)
	 *
	 * @param parentId 不填默认
	 * @param shopId   店铺id 必填
	 * @return
	 */
	List<CategoryVO> listByShopIdAndParenId(@Param("shopId") Long shopId, @Param("parentId") Long parentId);

	/**
	 * 根据分类id 获取分类下的子分类
	 *
	 * @param categoryId
	 * @return
	 */
    List<Category> getChildCategory(@Param("categoryId") Long categoryId);

	/**
	 * 批量更新分类状态（启用、禁用）
	 *
	 * @param categoryIds
	 * @param status
	 * @return
	 */
	int updateBatchOfStatus(@Param("categoryIds") List<Long> categoryIds, @Param("status") Integer status);

	/**
	 * 根据分类id列表，获取分类列表
	 *
	 * @param categoryIds 分类id
	 * @return 分类列表
	 */
    List<CategoryVO> getListByCategoryIds(@Param("categoryIds") Set<Long> categoryIds);

	/**
	 * 获取分类id列表
	 *
	 * @param shopId
	 * @param parentId
	 * @return
	 */
	List<Long> listCategoryId(@Param("shopId") Long shopId, @Param("parentId") Long parentId);

	/**
	 * 获取店铺签约成功的分类信息列表(仅关联的三级分类)
	 * @param shopId
	 * @param status
	 * @return
	 */
	List<CategoryVO> listSigningCategoryByShopIdAndStatus(@Param("shopId") Long shopId, @Param("status") Integer status);


	/**
	 * 获取整个平台的佣金比例
	 *
	 * @return 整个平台的佣金比例
	 */
    List<CategoryRateBO> listRate();

	/**
	 * 根据分类id列表，获取分类列表
	 *
	 * @param categoryIds 分类id列表
	 * @return 分类列表
	 */
	List<CategoryVO> listByCategoryIds(@Param("categoryIds") Set<Long> categoryIds);

	/**
	 * 获取当前节点所有父节点的分类ids，以及当前分类节点的父级节点的父级几点的分类ids
	 * @param categoryIds 当前分类节点ids
	 * @return 所有父级节点ids
	 */
    List<Long> getParentIdsByCategoryId(@Param("categoryIds") List<Long> categoryIds);


	/**
	 * 根据skuId获取skuId分类列表
	 * @param platformCommissionOrderItems skuId分类列表
	 * @return skuId分类列表
	 */
	List<PlatformCommissionOrderItemBO> listBySkuIds(@Param("platformCommissionOrderItems") List<PlatformCommissionOrderItemBO> platformCommissionOrderItems);

	/**
	 * 根据店铺id与父分类id获取分类id列表
	 * @param shopId
	 * @param parentId
	 * @return
	 */
	List<Long> listCategoryIdByShopIdAndParentId(@Param("shopId") Long shopId, @Param("parentId") Long parentId);

	Category getByName(@Param("name") String name, @Param("shopId") Long shopId,@Param("categoryId") Long categoryId);

	Long getCountByName(@Param("name") String name, @Param("shopId") Long shopId,@Param("categoryId") Long categoryId);

	List<CategoryVO> listbyParentId(@Param("parentId") Long parentId,@Param("shopId") Long shopId);
}
