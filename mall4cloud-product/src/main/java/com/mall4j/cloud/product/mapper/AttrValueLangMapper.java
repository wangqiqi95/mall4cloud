package com.mall4j.cloud.product.mapper;

import com.mall4j.cloud.product.model.AttrValueLang;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * 属性值-国际化表
 *
 * @author YXF
 * @date 2021-04-09 17:08:38
 */
public interface AttrValueLangMapper {

	/**
	 * 获取属性值-国际化表列表
	 *
	 * @return 属性值-国际化表列表
	 */
	List<AttrValueLang> list();

	/**
	 * 根据属性值-国际化表id获取属性值-国际化表
	 *
	 * @param attrValueId 属性值-国际化表id
	 * @return 属性值-国际化表
	 */
	AttrValueLang getByAttrValueId(@Param("attrValueId") Long attrValueId);

	/**
	 * 保存属性值-国际化表
	 *
	 * @param attrValueLang 属性值-国际化表
	 */
	void save(@Param("attrValueLang") AttrValueLang attrValueLang);

	/**
	 * 更新属性值-国际化表
	 *
	 * @param attrValueLang 属性值-国际化表
	 */
	void update(@Param("attrValueLang") AttrValueLang attrValueLang);

	/**
	 * 根据属性值-国际化表id删除属性值-国际化表
	 *
	 * @param attrValueId
	 */
	void deleteById(@Param("attrValueId") Long attrValueId);

	/**
	 * 批量保存属性值语言信息
	 *
	 * @param attrValueLangList
	 */
	void batchSave(@Param("attrValueLangList") List<AttrValueLang> attrValueLangList);

	/**
	 * 批量更新
	 *
	 * @param attrValueLangList
	 */
	void batchUpdate(@Param("attrValueLangList") List<AttrValueLang> attrValueLangList);

	/**
	 * 批量删除
	 *
	 * @param attrValueIds
	 */
    void deleteBatch(@Param("attrValueIds") List<Long> attrValueIds);

	/**
	 * 根据属性值id列表，删除属性值语言信息
	 *
	 * @param attrValueIds
	 */
	void deleteByAttrValueIds(@Param("attrValueIds") List<Long> attrValueIds);

	/**
	 * 根据属性id及语言id列表，删除属性值语言信息
	 * @param attrId
	 * @param langList
	 */
    void deleteByAttrIdAndLangs(@Param("attrId") Long attrId, @Param("langList") Collection<Integer> langList);
}
