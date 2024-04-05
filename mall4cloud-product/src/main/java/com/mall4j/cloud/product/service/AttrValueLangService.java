package com.mall4j.cloud.product.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.product.vo.AttrValueLangVO;
import com.mall4j.cloud.product.model.AttrValueLang;

import java.util.List;

/**
 * 属性值-国际化表
 *
 * @author YXF
 * @date 2021-04-09 17:08:38
 */
public interface AttrValueLangService {

	/**
	 * 分页获取属性值-国际化表列表
	 * @param pageDTO 分页参数
	 * @return 属性值-国际化表列表分页数据
	 */
	PageVO<AttrValueLang> page(PageDTO pageDTO);

	/**
	 * 根据属性值-国际化表id获取属性值-国际化表
	 *
	 * @param attrValueId 属性值-国际化表id
	 * @return 属性值-国际化表
	 */
	AttrValueLang getByAttrValueId(Long attrValueId);

	/**
	 * 保存属性值-国际化表
	 * @param attrValueLangList 属性值-国际化表
	 * @param attrValueId 属性值id
	 */
	void save(List<AttrValueLang> attrValueLangList, Long attrValueId);

	/**
	 * 更新属性值-国际化表
	 * @param attrValueLangList 属性值-国际化表
	 */
	void update(List<AttrValueLang> attrValueLangList);

	/**
	 * 根据属性值-国际化表id删除属性值-国际化表
	 * @param attrValueId 属性值-国际化表id
	 */
	void deleteById(Long attrValueId);

	/**
	 * 批量删除
	 * @param attrValueIds
	 */
    void deleteBatch(List<Long> attrValueIds);

	/**
	 * 根据属性值id列表，删除属性值语言信息
	 * @param attrValueIds
	 */
	void deleteByAttrValueIds(List<Long> attrValueIds);

	/**
	 * 批量保存
	 * @param mapAsList
	 */
    void saveBatch(List<AttrValueLang> mapAsList);
}
