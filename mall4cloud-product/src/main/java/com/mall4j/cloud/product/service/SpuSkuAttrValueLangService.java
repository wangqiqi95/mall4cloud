package com.mall4j.cloud.product.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.product.dto.SpuSkuAttrValueDTO;
import com.mall4j.cloud.product.model.SpuSkuAttrValueLang;

import java.util.List;

/**
 * 商品sku销售属性关联信息-国际化
 *
 * @author YXF
 * @date 2021-04-09 17:08:38
 */
public interface SpuSkuAttrValueLangService {

	/**
	 * 分页获取商品sku销售属性关联信息-国际化列表
	 * @param pageDTO 分页参数
	 * @return 商品sku销售属性关联信息-国际化列表分页数据
	 */
	PageVO<SpuSkuAttrValueLang> page(PageDTO pageDTO);

	/**
	 * 根据商品sku销售属性关联信息-国际化id获取商品sku销售属性关联信息-国际化
	 *
	 * @param spuSkuAttrId 商品sku销售属性关联信息-国际化id
	 * @return 商品sku销售属性关联信息-国际化
	 */
	SpuSkuAttrValueLang getBySpuSkuAttrId(Long spuSkuAttrId);

	/**
	 * 保存商品sku销售属性关联信息-国际化
	 * @param spuSkuAttrValueLang 商品sku销售属性关联信息-国际化
	 */
	void save(SpuSkuAttrValueLang spuSkuAttrValueLang);

	/**
	 * 更新商品sku销售属性关联信息-国际化
	 * @param spuSkuAttrValueLang 商品sku销售属性关联信息-国际化
	 */
	void update(SpuSkuAttrValueLang spuSkuAttrValueLang);

	/**
	 * 根据商品sku销售属性关联信息-国际化id删除商品sku销售属性关联信息-国际化
	 * @param spuSkuAttrId 商品sku销售属性关联信息-国际化id
	 */
	void deleteById(Long spuSkuAttrId);

}
