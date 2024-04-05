package com.mall4j.cloud.product.mapper;

import com.mall4j.cloud.product.dto.CategoryDTO;
import com.mall4j.cloud.product.dto.CategoryLangDTO;
import com.mall4j.cloud.product.model.CategoryLang;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分类-国际化表
 *
 * @author YXF
 * @date 2021-04-22 17:48:16
 */
public interface CategoryLangMapper {

	/**
	 * 获取分类-国际化表列表
	 *
	 * @return 分类-国际化表列表
	 */
	List<CategoryLang> list();

	/**
	 * 根据分类-国际化表id获取分类-国际化表
	 *
	 * @param categoryId 分类-国际化表id
	 * @return 分类-国际化表
	 */
	CategoryLang getByCategoryId(@Param("categoryId") Long categoryId);

	/**
	 * 保存分类-国际化表
	 *
	 * @param categoryLang 分类-国际化表
	 */
	void save(@Param("categoryLang") CategoryLang categoryLang);

	/**
	 * 更新分类-国际化表
	 *
	 * @param categoryLang 分类-国际化表
	 */
	void update(@Param("categoryLang") CategoryLang categoryLang);

	/**
	 * 根据分类-国际化表id删除分类-国际化表
	 *
	 * @param categoryId
	 */
	void deleteById(@Param("categoryId") Long categoryId);

	/**
	 * 批量保存多语言信息
	 *
	 * @param categoryLangList
	 */
	void batchSave(@Param("categoryLangList") List<CategoryLang> categoryLangList);

	/**
	 * 批量更新多语言信息
	 *
	 * @param categoryLangList
	 */
	void batchUpdate(@Param("categoryLangList") List<CategoryLang> categoryLangList);

	/**
	 * 批量删除
	 *
	 * @param langIds
	 * @param categoryId
	 */
    void batchDelete(@Param("langIds") List<Integer> langIds, @Param("categoryId") Long categoryId);

	/**
	 * 查询分类名是否存在
	 *
	 * @param categoryDTO
	 * @return 已存在的名称列表
	 */
	List<String> existCategoryName(@Param("category") CategoryDTO categoryDTO);
}
