package com.mall4j.cloud.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.common.database.util.PageAdapter;
import com.mall4j.cloud.common.product.dto.AttrLangDTO;
import com.mall4j.cloud.product.dto.AttrDTO;
import com.mall4j.cloud.product.model.Attr;
import com.mall4j.cloud.common.product.vo.AttrVO;
import com.mall4j.cloud.product.model.AttrLang;
import com.mall4j.cloud.product.vo.AppAttrValuesVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:23
 */
public interface AttrMapper extends BaseMapper<Attr> {

	/**
	 * 获取属性信息列表
	 *
	 * @param pageAdapter 分页参数
	 * @param attrDTO     属性数据
	 * @return 属性信息列表
	 */
	List<AttrVO> listAttr(@Param("page") PageAdapter pageAdapter, @Param("attr") AttrDTO attrDTO);

	/**
	 * 获取属性总数
	 *
	 * @param attrDTO
	 * @return
	 */
	Long countAttr(@Param("attr") AttrDTO attrDTO);

	/**
	 * 根据属性信息id获取属性信息
	 *
	 * @param attrId 属性信息id
	 * @return 属性信息
	 */
	AttrVO getByAttrId(@Param("attrId") Long attrId);

	/**
	 * 保存属性信息
	 *
	 * @param attr 属性信息
	 */
	void saveAttr(@Param("attr") Attr attr);

	/**
	 * 更新属性信息
	 *
	 * @param attr 属性信息
	 */
	void updateAttr(@Param("attr") Attr attr);

	/**
	 * 根据属性信息id删除属性信息
	 *
	 * @param attrId
	 */
	void deleteById(@Param("attrId") Long attrId);

	/**
	 * 根据分类和属性类型，获取对应的属性列表
	 *
	 * @param attrType
	 * @param categoryId
	 * @param shopId
	 * @return
	 */
	List<AttrVO> getAttrsByCategoryIdAndAttrType(@Param("attrType") Integer attrType, @Param("categoryId") Long categoryId, @Param("shopId") Long shopId);


	/**
	 * 统计属性名的数量
	 *
	 * @param attrLangList
	 * @param shopId
	 * @param attrId
	 * @return
	 */
    List<AttrLang> countAttrName(@Param("attrLangList") List<AttrLangDTO> attrLangList, @Param("shopId") Long shopId, @Param("attrId") Long attrId);

	List<AppAttrValuesVo> listAppAttrValues(@Param("attrIds") List<Long> attrIds);
}
