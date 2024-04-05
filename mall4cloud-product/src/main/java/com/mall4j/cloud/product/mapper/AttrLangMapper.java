package com.mall4j.cloud.product.mapper;

import com.mall4j.cloud.product.model.AttrLang;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 属性-国际化表
 *
 * @author YXF
 * @date 2021-04-09 17:08:38
 */
public interface AttrLangMapper {

	/**
	 * 获取属性-国际化表列表
	 *
	 * @return 属性-国际化表列表
	 */
	List<AttrLang> list();

	/**
	 * 根据属性-国际化表id获取属性-国际化表
	 *
	 * @param attrId 属性-国际化表id
	 * @return 属性-国际化表
	 */
	AttrLang getByAttrId(@Param("attrId") Long attrId);

	/**
	 * 保存属性-国际化表
	 *
	 * @param attrLang 属性-国际化表
	 */
	void save(@Param("attrLang") AttrLang attrLang);

	/**
	 * 更新属性-国际化表
	 *
	 * @param attrLang 属性-国际化表
	 */
	void update(@Param("attrLang") AttrLang attrLang);

	/**
	 * 根据属性-国际化表id删除属性-国际化表
	 *
	 * @param attrId
	 */
	void deleteById(@Param("attrId") Long attrId);

	/**
	 * 批量保存属性语言信息
	 *
	 * @param attrLangList
	 */
	void batchSave(@Param("attrLangList") List<AttrLang> attrLangList);

	/**
	 * 根据attrId 获取属性
	 *
	 * @param attrId
	 * @return
	 */
	List<AttrLang> listByAttrId(@Param("attrId") Long attrId);

	/**
	 * 批量更新属性语言信息
	 *
	 * @param attrLangList
	 */
	void batchUpdate(@Param("attrLangList") List<AttrLang> attrLangList);

	/**
	 * 批量删除属性语言信息
	 * @param langList
	 * @param attrId
	 */
	void batchDelete(@Param("langList") Collection<Integer> langList, @Param("attrId") Long attrId);
}
