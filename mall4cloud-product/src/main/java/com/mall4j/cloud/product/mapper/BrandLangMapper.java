package com.mall4j.cloud.product.mapper;

import com.mall4j.cloud.api.product.dto.BrandShopDTO;
import com.mall4j.cloud.product.model.BrandLang;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 品牌-国际化表
 *
 * @author YXF
 * @date 2021-04-26 15:17:37
 */
public interface BrandLangMapper {

	/**
	 * 获取品牌-国际化表列表
	 *
	 * @return 品牌-国际化表列表
	 */
	List<BrandLang> list();

	/**
	 * 根据品牌-国际化表id获取品牌-国际化表
	 *
	 * @param brandId 品牌-国际化表id
	 * @return 品牌-国际化表
	 */
	BrandLang getByBrandId(@Param("brandId") Long brandId);

	/**
	 * 保存品牌-国际化表
	 *
	 * @param brandLang 品牌-国际化表
	 */
	void save(@Param("brandLang") BrandLang brandLang);

	/**
	 * 更新品牌-国际化表
	 *
	 * @param brandLang 品牌-国际化表
	 */
	void update(@Param("brandLang") BrandLang brandLang);

	/**
	 * 根据品牌-国际化表id删除品牌-国际化表
	 *
	 * @param brandId
	 */
	void deleteById(@Param("brandId") Long brandId);

	/**
	 * 批量保存
	 *
	 * @param brandLangList
	 */
	void batchSave(@Param("brandLangList") List<BrandLang> brandLangList);

	/**
	 * 批量更新
	 *
	 * @param brandLangList
	 */
	void batchUpdate(@Param("brandLangList") List<BrandLang> brandLangList);

	/**
	 * 批量插入品牌信息
	 *
	 * @param brandList
	 * @param lang
	 */
    void insertBatch(@Param("brandList") List<BrandShopDTO> brandList, @Param("lang") Integer lang);

	/**
	 * 根据品牌id列表删除品牌名称信息
	 *
	 * @param brandIdList
	 */
	void deleteBatchByBrandIds(@Param("brandIdList") List<Long> brandIdList);

	/**
	 * 根据品牌id，获取品牌国际化信息列表
	 *
	 * @param brandId 品牌id
	 * @return 品牌国际化信息列表
	 */
	List<Integer> langIdsByBrandId(@Param("brandId") Long brandId);

	/**
	 * 批量删除品牌信息
	 *
	 * @param langIds 语言id列表
	 * @param brandId 品牌id
	 */
	void batchDelete(@Param("langIds") List<Integer> langIds, @Param("brandId") Long brandId);

	/**
	 * 根据品牌名，获取品牌列表
	 *
	 * @param brandNames 品牌名
	 * @param shopId 店铺id
	 * @return 品牌列表
	 */
    List<BrandLang> listByBrandNames(@Param("brandNames") Set<String> brandNames, @Param("shopId") Long shopId);
}
