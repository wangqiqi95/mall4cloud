package com.mall4j.cloud.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.product.dto.AttrDTO;
import com.mall4j.cloud.product.model.Attr;
import com.mall4j.cloud.common.product.vo.AttrVO;
import com.mall4j.cloud.product.vo.AppAttrValuesVo;
import com.mall4j.cloud.product.vo.AppAttrVo;

import java.util.List;

/**
 * 属性信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:23
 */
public interface AttrService extends IService<Attr> {

	/**
	 * 分页获取属性信息列表
	 * @param pageDTO 分页参数
	 * @param attrDTO
	 * @return 属性信息列表分页数据
	 */
	PageVO<AttrVO> page(PageDTO pageDTO, AttrDTO attrDTO);

	/**
	 * 根据属性信息id获取属性信息
	 *
	 * @param attrId 属性信息id
	 * @return 属性信息
	 */
	AttrVO getByAttrId(Long attrId);
	/**
	 * 保存属性信息
	 * @param attrDTO 属性信息
	 */
	void save(AttrDTO attrDTO);

	/**
	 * 更新属性信息
	 * @param attrDTO 属性信息
	 */
	void update(AttrDTO attrDTO);

	/**
	 * 根据属性信息id删除属性信息
	 * @param attrId
	 */
	void deleteById(Long attrId);

	/**
	 * 根据分类和属性类型，获取对应的属性列表
	 * @param categoryId
	 * @return
	 */
	List<AttrVO> getAttrsByCategoryIdAndAttrType(Long categoryId);

	/**
	 * 根据属性id获取属性下的分类id列表
	 * @param attrId
	 * @return
	 */
	List<Long> getAttrOfCategoryIdByAttrId(Long attrId);

	/**
	 * 清除属性关联的分类列表中属性列表的缓存
	 * @param categoryIds 属性关联的分类列表
	 */
	void removeAttrByCategoryId(List<Long> categoryIds);

	/**
	 * 获取店铺中的销售属性
	 * @param shopId
	 * @return
	 */
	List<AttrVO> getShopAttrs(Long shopId);

	List<AppAttrVo> listAppAttrValues(List<Long> attrIds);
}
