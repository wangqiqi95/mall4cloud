package com.mall4j.cloud.product.mapper;

import com.mall4j.cloud.product.model.SpuAttrValueLang;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性值-国际化表
 *
 * @author YXF
 * @date 2021-04-15 16:47:33
 */
public interface SpuAttrValueLangMapper {

	/**
	 * 获取属性值-国际化表列表
	 * @return 属性值-国际化表列表
	 */
	List<SpuAttrValueLang> list();

	/**
	 * 根据属性值-国际化表id获取属性值-国际化表
	 *
	 * @param spuAttrValueId 属性值-国际化表id
	 * @return 属性值-国际化表
	 */
	SpuAttrValueLang getBySpuAttrValueId(@Param("spuAttrValueId") Long spuAttrValueId);

	/**
	 * 保存属性值-国际化表
	 * @param spuAttrValueLang 属性值-国际化表
	 */
	void save(@Param("spuAttrValueLang") SpuAttrValueLang spuAttrValueLang);

	/**
	 * 更新属性值-国际化表
	 * @param spuAttrValueLang 属性值-国际化表
	 */
	void update(@Param("spuAttrValueLang") SpuAttrValueLang spuAttrValueLang);

	/**
	 * 根据属性值-国际化表id删除属性值-国际化表
	 * @param spuAttrValueId
	 */
	void deleteById(@Param("spuAttrValueId") Long spuAttrValueId);
}
