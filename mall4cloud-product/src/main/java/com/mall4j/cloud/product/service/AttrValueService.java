package com.mall4j.cloud.product.service;

import com.mall4j.cloud.common.product.vo.AttrVO;
import com.mall4j.cloud.product.dto.AttrDTO;
import com.mall4j.cloud.product.dto.AttrValueDTO;
import com.mall4j.cloud.product.model.Attr;
import com.mall4j.cloud.product.model.AttrValue;

import java.util.List;
import java.util.Set;

/**
 * 属性值信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
public interface AttrValueService {
	/**
	 * 根据属性值信息和属性id，保存属性值信息
	 * @param attrValueList
	 * @param attrId
	 */
    void save(List<AttrValueDTO> attrValueList, Long attrId);

	/**
	 * 根据属性值信息和属性id，更新属性值信息
	 * @param attrDTO
	 * @param dbAttr
	 */
	void update(AttrDTO attrDTO, AttrVO dbAttr);

	/**
	 * 根据属性id， 删除属性值信息
	 * @param attrId
	 */
    void deleteByAttrId(Long attrId);
}
