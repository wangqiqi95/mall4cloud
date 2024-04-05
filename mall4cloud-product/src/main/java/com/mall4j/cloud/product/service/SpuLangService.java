package com.mall4j.cloud.product.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.product.vo.SpuLangVO;
import com.mall4j.cloud.product.model.Spu;
import com.mall4j.cloud.product.model.SpuLang;

import java.util.List;

/**
 * 商品-国际化表
 *
 * @author YXF
 * @date 2021-04-09 17:08:38
 */
public interface SpuLangService {

	/**
	 * 分页获取商品-国际化表列表
	 * @param pageDTO 分页参数
	 * @return 商品-国际化表列表分页数据
	 */
	PageVO<SpuLang> page(PageDTO pageDTO);

	/**
	 * 根据商品-国际化表id获取商品-国际化表
	 *
	 * @param spuId 商品-国际化表id
	 * @return 商品-国际化表
	 */
	SpuLang getBySpuId(Long spuId);

	/**
	 * 批量保存商品-国际化表
	 * @param spuLangList 商品-国际化表
	 */
	void batchSave(List<SpuLang> spuLangList);

	/**
	 * 批量更新商品-国际化表
	 * @param spuLangList 商品-国际化表
	 */
	void batchUpdate(List<SpuLang> spuLangList);

	/**
	 * 根据商品-国际化表id删除商品-国际化表
	 * @param spuId 商品-国际化表id
	 */
	void deleteById(Long spuId);

	/**
	 * 更新
	 * @param spuLangList 商品国际化信息
	 * @param spuId 商品id
	 */
    void update(List<SpuLang> spuLangList, Long spuId);
}
