package com.mall4j.cloud.product.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.product.model.SkuLang;

import java.util.List;

/**
 * sku-国际化表
 *
 * @author YXF
 * @date 2021-04-09 17:08:38
 */
public interface SkuLangService {

	/**
	 * 分页获取sku-国际化表列表
	 * @param pageDTO 分页参数
	 * @return sku-国际化表列表分页数据
	 */
	PageVO<SkuLang> page(PageDTO pageDTO);

	/**
	 * 根据sku-国际化表id获取sku-国际化表
	 *
	 * @param skuId sku-国际化表id
	 * @return sku-国际化表
	 */
	SkuLang getBySkuId(Long skuId);

	/**
	 * 保存sku-国际化表
	 * @param skuLang sku-国际化表
	 */
	void save(SkuLang skuLang);

	/**
	 * 更新sku-国际化表
	 * @param skuLang sku-国际化表
	 */
	void update(SkuLang skuLang);

	/**
	 * 根据sku-国际化表id删除sku-国际化表
	 * @param skuId sku-国际化表id
	 */
	void deleteById(Long skuId);

	/**
	 * 批量保存sku国际化信息
	 * @param skuLangList
	 */
    void batchSave(List<SkuLang> skuLangList);
}
