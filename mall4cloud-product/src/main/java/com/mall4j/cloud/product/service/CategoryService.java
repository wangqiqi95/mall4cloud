package com.mall4j.cloud.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.api.product.bo.PlatformCommissionOrderItemBO;
import com.mall4j.cloud.api.product.dto.IphSyncCategoryDto;
import com.mall4j.cloud.common.product.vo.CategoryVO;
import com.mall4j.cloud.common.product.vo.app.CategoryAppVO;
import com.mall4j.cloud.product.bo.CategoryRateBO;
import com.mall4j.cloud.product.dto.CategoryDTO;
import com.mall4j.cloud.product.model.Category;

import java.util.List;
import java.util.Set;

/**
 * 分类信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
public interface CategoryService extends IService<Category> {

	/**
	 * 根据分类信息(分类、上级分类名称列表)
	 *
	 * @param categoryId 分类信息id
	 * @return 分类信息
	 */
	CategoryVO getInfo(Long categoryId);

	/**
	 * 根据分类信息（缓存）
	 *
	 * @param categoryId 分类信息id
	 * @return 分类信息
	 */
	CategoryVO getById(Long categoryId);

	/**
	 * 保存分类信息
	 * @param categoryDTO 分类信息
	 */
	void save(CategoryDTO categoryDTO);

	/**
	 * 保存分类信息
	 * @param categoryDTO 分类信息
	 */
	Long saveAndReturnId(CategoryDTO categoryDTO);

	/**
	 * 更新分类信息
	 * @param categoryDTO 分类信息
	 */
	void update(CategoryDTO categoryDTO);

	/**
	 * 根据分类信息id删除分类信息
     * @param categoryId 分类id
     * @param shopId 店铺id
     */
	void deleteById(Long categoryId, Long shopId);

	/**
	 * 获取分类列表(未删除的分类--启用、未启用状态的分类)
	 * @param shopId 店铺id 必填
	 * @return
	 */
	List<CategoryAppVO> list(Long shopId);

	/**
	 * 获取分类id列表
	 * @param shopId
	 * @param parentId
	 * @return
	 */
	List<Long> listCategoryId(Long shopId, Long parentId);

	/**
	 * 根据shopId 和 categoruId 清除分类缓存
	 * @param shopId
	 * @param categoryId
	 */
	void removeCategoryCache(Long shopId, Long categoryId);

	/**
	 * 分类的启用和禁用
	 * @param categoryDTO
	 * @return
	 */
    void categoryEnableOrDisable(CategoryDTO categoryDTO);

	/**
	 *  获取分类的pathName集合
	 * @param categories 分类集合
	 */
	void getPathNames(List<CategoryVO> categories);

//	/**
//	 * 获取分类列表 (仅返回启用状态的分类)
//	 * @param parentId 上级分类id
//	 * @param shopId 店铺id
//	 * @return
//	 */
//	List<CategoryVO> categoryList(Long shopId, Long parentId);

	/**
	 * 根据店铺id和上级id，获取分类列表
	 * @param shopId 店铺id
	 * @param parentId 上级分类id
	 * @param lang 语言
	 * @return 分类列表
	 */
	List<CategoryAppVO> listByShopIdAndParenId(Long shopId, Long parentId, Integer lang);

	/**
	 * 获取分类列表 (仅获取启用状态以及包含三级分类的分类信息)
	 * @param shopId 店铺id
	 * @param lang 语言
	 * @return 分类列表
	 */
	List<CategoryAppVO> shopCategoryList(Long shopId);

	/**
	 * 获取整个平台的佣金比例
	 * @return 整个平台的佣金比例
	 */
	List<CategoryRateBO> listRate();


	/**
	 * 根据店铺id获取店铺签约的分类列表
	 * @param shopId
	 * @param lang
	 * @return
	 */
    List<CategoryAppVO> listSigningCategory(Long shopId, Integer lang);

	/**
	 * 根据分类id列表，获取分类列表
	 * @param categoryIds 分类id列表
	 * @return 分类列表
	 */
	List<CategoryVO> listByCategoryIds(Set<Long> categoryIds);

	/**
	 * 获取店铺二级分类
	 * @param shopId
	 * @return
	 */
	List<CategoryVO> listAndLangInfoByShopId(Long shopId);

	/**
	 * 获取店铺签约分类及国际化信息
	 * @param shopId
	 * @return
	 */
	List<CategoryVO> getShopSigningCategoryAndLangInfo(Long shopId);

	/**
	 * 平台分类
	 * @return
	 * @param shopId
	 */
	List<CategoryAppVO> platformCategories(Long shopId);

	/**
	 * 获取当前节点所有父节点的分类ids，以及当前分类节点的父级节点的父级几点的分类ids
	 * @param categoryIds 当前分类节点ids
	 * @return 所有父级节点ids
	 */
    List<Long> getParentIdsByCategoryId(List<Long> categoryIds);

	/**
	 * 根据skuId获取skuId分类列表
	 * @param platformCommissionOrderItems skuId分类列表
	 * @return skuId分类列表
	 */
	List<PlatformCommissionOrderItemBO> listBySkuIds(List<PlatformCommissionOrderItemBO> platformCommissionOrderItems);

	/**
	 * 爱铺货分类信息同步入库
	 * @param iphSyncCategoryDto
	 * @return
	 */
	Long sycnIPH(IphSyncCategoryDto iphSyncCategoryDto);

	Boolean categoryShopUpdateBatch(List<CategoryDTO> categoryDTOList);

	List<CategoryVO> listbyParntId(Long parentId, Long shopId);
}
