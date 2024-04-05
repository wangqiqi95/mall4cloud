package com.mall4j.cloud.product.mapper;

import com.mall4j.cloud.common.product.dto.SpuSkuAttrValueDTO;
import com.mall4j.cloud.product.model.SpuSkuAttrValueLang;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品sku销售属性关联信息-国际化
 *
 * @author YXF
 * @date 2021-04-09 17:08:38
 */
public interface SpuSkuAttrValueLangMapper {

	/**
	 * 获取商品sku销售属性关联信息-国际化列表
	 *
	 * @return 商品sku销售属性关联信息-国际化列表
	 */
	List<SpuSkuAttrValueLang> list();

	/**
	 * 根据商品sku销售属性关联信息-国际化id获取商品sku销售属性关联信息-国际化
	 *
	 * @param spuSkuAttrId 商品sku销售属性关联信息-国际化id
	 * @return 商品sku销售属性关联信息-国际化
	 */
	SpuSkuAttrValueLang getBySpuSkuAttrId(@Param("spuSkuAttrId") Long spuSkuAttrId);

	/**
	 * 保存商品sku销售属性关联信息-国际化
	 *
	 * @param spuSkuAttrValueLang 商品sku销售属性关联信息-国际化
	 */
	void save(@Param("spuSkuAttrValueLang") SpuSkuAttrValueLang spuSkuAttrValueLang);

	/**
	 * 更新商品sku销售属性关联信息-国际化
	 *
	 * @param spuSkuAttrValueLang 商品sku销售属性关联信息-国际化
	 */
	void update(@Param("spuSkuAttrValueLang") SpuSkuAttrValueLang spuSkuAttrValueLang);

	/**
	 * 根据商品sku销售属性关联信息-国际化id删除商品sku销售属性关联信息-国际化
	 *
	 * @param spuSkuAttrId
	 */
	void deleteById(@Param("spuSkuAttrId") Long spuSkuAttrId);

	/**
	 * 批量保存
	 *
	 * @param spuSkuAttrValueLangList
	 */
	void batchSave(@Param("spuSkuAttrValueLangList") List<SpuSkuAttrValueLang> spuSkuAttrValueLangList);

}
