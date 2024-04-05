package com.mall4j.cloud.product.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.product.model.BrandLang;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 品牌-国际化表
 *
 * @author YXF
 * @date 2021-04-26 15:17:37
 */
public interface BrandLangService {

	/**
	 * 分页获取品牌-国际化表列表
	 * @param pageDTO 分页参数
	 * @return 品牌-国际化表列表分页数据
	 */
	PageVO<BrandLang> page(PageDTO pageDTO);

	/**
	 * 根据品牌-国际化表id获取品牌-国际化表
	 *
	 * @param brandId 品牌-国际化表id
	 * @return 品牌-国际化表
	 */
	BrandLang getByBrandId(Long brandId);

	/**
	 * 保存品牌-国际化表
	 * @param brandLangList 品牌-国际化表
	 * @param brandId 品牌id
	 */
	void save(List<BrandLang> brandLangList, Long brandId);

	/**
	 * 更新品牌-国际化表
	 * @param brandLangList 品牌-国际化表
	 * @param brandId 品牌id
	 */
	void update(List<BrandLang> brandLangList, Long brandId);

	/**
	 * 根据品牌-国际化表id删除品牌-国际化表
	 * @param brandId 品牌-国际化表id
	 */
	void deleteById(Long brandId);

	/**
	 * 根据品牌名，获取品牌列表
	 *
	 * @param brandNames 品牌名
	 * @param shopId 店铺id
	 * @return 品牌列表
	 */
    List<BrandLang> listByBrandNames(Set<String> brandNames, Long shopId);
}
