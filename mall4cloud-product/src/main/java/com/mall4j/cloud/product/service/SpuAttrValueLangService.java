package com.mall4j.cloud.product.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.product.model.SpuAttrValueLang;

/**
 * 属性值-国际化表
 *
 * @author YXF
 * @date 2021-04-15 16:47:33
 */
public interface SpuAttrValueLangService {

	/**
	 * 分页获取属性值-国际化表列表
	 * @param pageDTO 分页参数
	 * @return 属性值-国际化表列表分页数据
	 */
	PageVO<SpuAttrValueLang> page(PageDTO pageDTO);

	/**
	 * 根据属性值-国际化表id获取属性值-国际化表
	 *
	 * @param spuAttrValueId 属性值-国际化表id
	 * @return 属性值-国际化表
	 */
	SpuAttrValueLang getBySpuAttrValueId(Long spuAttrValueId);

	/**
	 * 保存属性值-国际化表
	 * @param spuAttrValueLang 属性值-国际化表
	 */
	void save(SpuAttrValueLang spuAttrValueLang);

	/**
	 * 更新属性值-国际化表
	 * @param spuAttrValueLang 属性值-国际化表
	 */
	void update(SpuAttrValueLang spuAttrValueLang);

	/**
	 * 根据属性值-国际化表id删除属性值-国际化表
	 * @param spuAttrValueId 属性值-国际化表id
	 */
	void deleteById(Long spuAttrValueId);
}
