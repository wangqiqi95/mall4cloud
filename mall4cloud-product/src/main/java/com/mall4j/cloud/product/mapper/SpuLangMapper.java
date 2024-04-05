package com.mall4j.cloud.product.mapper;

import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.product.model.SpuLang;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品-国际化表
 *
 * @author YXF
 * @date 2021-04-09 17:08:38
 */
public interface SpuLangMapper {

	/**
	 * 获取商品-国际化表列表
	 *
	 * @return 商品-国际化表列表
	 */
	List<SpuLang> list();

	/**
	 * 根据商品-国际化表id获取商品-国际化表
	 *
	 * @param spuId 商品-国际化表id
	 * @return 商品-国际化表
	 */
	SpuLang getBySpuId(@Param("spuId") Long spuId);

	/**
	 * 保存商品-国际化表
	 *
	 * @param spuLang 商品-国际化表
	 */
	void save(@Param("spuLang") SpuLang spuLang);

	/**
	 * 批量更新商品-国际化表
	 *
	 * @param spuLangList 商品-国际化表
	 */
	void batchUpdate(@Param("spuLangList") List<SpuLang> spuLangList);

	/**
	 * 根据商品-国际化表id删除商品-国际化表
	 *
	 * @param spuId
	 */
	void deleteById(@Param("spuId") Long spuId);

	/**
	 * 批量保存
	 * @param spuLangList 商品国际化信息
	 */
	void batchSave(@Param("spuLangList") List<SpuLang> spuLangList);

	/**
	 * 批量删除
	 * @param spuId 商品id
	 * @param langList 语言列表
	 */
    void deleteBatchBySpuIdAndLang(@Param("spuId") Long spuId, @Param("langList") List<Integer> langList);

	/**
	 * 获取商品的语言id列表
	 *
	 * @param spuId 商品id
	 * @return 语言id列表
	 */
	List<Integer> listLangId(@Param("spuId") Long spuId);
}
