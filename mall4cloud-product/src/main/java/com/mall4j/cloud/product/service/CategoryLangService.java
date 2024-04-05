package com.mall4j.cloud.product.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.product.vo.CategoryVO;
import com.mall4j.cloud.product.dto.CategoryDTO;
import com.mall4j.cloud.product.dto.CategoryLangDTO;
import com.mall4j.cloud.product.model.CategoryLang;

import java.util.List;

/**
 * 分类-国际化表
 *
 * @author YXF
 * @date 2021-04-22 17:48:16
 */
public interface CategoryLangService {

	/**
	 * 分页获取分类-国际化表列表
	 * @param pageDTO 分页参数
	 * @return 分类-国际化表列表分页数据
	 */
	PageVO<CategoryLang> page(PageDTO pageDTO);

	/**
	 * 根据分类-国际化表id获取分类-国际化表
	 *
	 * @param categoryId 分类-国际化表id
	 * @return 分类-国际化表
	 */
	CategoryLang getByCategoryId(Long categoryId);

	/**
	 * 保存分类-国际化表
	 * @param categoryLangList 分类-国际化表
	 * @param categoryId 分类-国际化表
	 */
	void save(List<CategoryLangDTO> categoryLangList, Long categoryId);

	/**
	 * 更新分类-国际化表
	 * @param categoryLangList 分类-国际化表
	 * @param categoryDb
	 */
	void update(List<CategoryLangDTO> categoryLangList, CategoryVO categoryDb);

	/**
	 * 根据分类-国际化表id删除分类-国际化表
	 * @param categoryId 分类-国际化表id
	 */
	void deleteById(Long categoryId);

	/**
	 * 批量保存
	 * @param list 保存的分类数据列表
	 */
	void batchSave(List<CategoryLang> list);

	/**
	 * 分类名是否已存在
	 * @param categoryDTO
	 */
    void existCategoryName(CategoryDTO categoryDTO);

}
