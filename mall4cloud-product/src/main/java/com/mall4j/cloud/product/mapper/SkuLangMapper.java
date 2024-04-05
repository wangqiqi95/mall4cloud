package com.mall4j.cloud.product.mapper;

import com.mall4j.cloud.product.model.SkuLang;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * sku-国际化表
 *
 * @author YXF
 * @date 2021-04-09 17:08:38
 */
public interface SkuLangMapper {

	/**
	 * 获取sku-国际化表列表
	 *
	 * @return sku-国际化表列表
	 */
	List<SkuLang> list();

	/**
	 * 根据sku-国际化表id获取sku-国际化表
	 *
	 * @param skuId sku-国际化表id
	 * @return sku-国际化表
	 */
	SkuLang getBySkuId(@Param("skuId") Long skuId);

	/**
	 * 保存sku-国际化表
	 *
	 * @param skuLang sku-国际化表
	 */
	void save(@Param("skuLang") SkuLang skuLang);

	/**
	 * 更新sku-国际化表
	 *
	 * @param skuLang sku-国际化表
	 */
	void update(@Param("skuLang") SkuLang skuLang);

	/**
	 * 根据sku-国际化表id删除sku-国际化表
	 *
	 * @param skuId
	 */
	void deleteById(@Param("skuId") Long skuId);

	/**
	 * 批量保存sku国际化信息
	 *
	 * @param skuLangList
	 */
    void batchSave(@Param("skuLangList") List<SkuLang> skuLangList);

}
