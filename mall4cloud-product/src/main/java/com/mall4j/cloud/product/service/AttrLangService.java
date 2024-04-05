package com.mall4j.cloud.product.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.product.dto.AttrLangDTO;
import com.mall4j.cloud.common.product.vo.AttrVO;
import com.mall4j.cloud.product.model.AttrLang;

import java.util.List;
import java.util.Set;

/**
 * 属性-国际化表
 *
 * @author YXF
 * @date 2021-04-09 17:08:38
 */
public interface AttrLangService {

	/**
	 * 分页获取属性-国际化表列表
	 * @param pageDTO 分页参数
	 * @return 属性-国际化表列表分页数据
	 */
	PageVO<AttrLang> page(PageDTO pageDTO);

	/**
	 * 根据属性-国际化表id获取属性-国际化表
	 *
	 * @param attrId 属性-国际化表id
	 * @return 属性-国际化表
	 */
	AttrLang getByAttrId(Long attrId);

	/**
	 * 保存属性-国际化表
	 * @param attrLangList 属性-国际化表
	 * @param attrId 属性id
	 */
	void save(List<AttrLang> attrLangList, Long attrId);

	/**
	 * 更新属性-国际化表
	 * @param attrLangList 属性-国际化表
	 * @param attrVO 属性
	 * @return 新增语言id数组
	 */
	void update(List<AttrLang> attrLangList, AttrVO attrVO);

	/**
	 * 根据属性-国际化表id删除属性-国际化表
	 * @param attrId 属性-国际化表id
	 */
	void deleteById(Long attrId);
}
