package com.mall4j.cloud.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.common.product.vo.app.SpuAttrValueAppVO;
import com.mall4j.cloud.product.model.SpuAttrValue;
import com.mall4j.cloud.common.product.vo.SpuAttrValueVO;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 商品规格属性关联信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
public interface SpuAttrValueMapper extends BaseMapper<SpuAttrValue> {

	/**
	 * 保存商品规格属性关联信息
	 *
	 * @param spuAttrValue 商品规格属性关联信息
	 */
	void saveAttrValue(@Param("spuAttrValue") SpuAttrValue spuAttrValue);

	/**
	 * 更新商品规格属性关联信息
	 *
	 * @param spuAttrValue 商品规格属性关联信息
	 */
	void updateSpuAttrValue(@Param("spuAttrValue") SpuAttrValue spuAttrValue);

	/**
	 * 批量保存
	 *
	 * @param spuAttrValues 规格list
	 */
	void batchSave(@Param("spuAttrValues") List<SpuAttrValue> spuAttrValues);

	/**
	 * 根据spuId删除对应规格属性
	 *
	 * @param spuId id
	 */
	void deleteBySpuId(Long spuId);

	/**
	 * 根据属性和属性值id列表删除商品属性关联信息
	 *
	 * @param attrId       属性id
	 * @param attrValueIds 属性值id
	 */
    void deleteByAttId(@Param("attrId") Long attrId, @Param("attrValueIds") List<Long> attrValueIds);

	/**
	 * 根据属性和分类id列表删除商品属性关联信息
	 *
	 * @param attrId      属性id
	 * @param categoryIds 属性值id
	 */
    void deleteByCategoryIds(@Param("attrId") Long attrId, @Param("categoryIds") List<Long> categoryIds);

	/**
	 * 根据spuId获取商品属性列表
	 *
	 * @param spuId
	 * @return
	 */
	List<SpuAttrValueVO> getSpuAttrsBySpuId(@Param("spuId") Long spuId);

	/**
	 * 批量更新商品属性列表
	 *
	 * @param spuAttrValues
	 */
	void batchUpdate(@Param("spuAttrValues") List<SpuAttrValue> spuAttrValues);

	/**
	 * 批量删除商品属性
	 *
	 * @param spuAttrValueIds
	 */
	void deleteBatch(@Param("spuAttrValueIds") List<Long> spuAttrValueIds);

	/**
	 * 根据属性值id，获取spuId列表
	 *
	 * @param attrValueIds
	 * @return
	 */
	List<Long> spuIdListByAttrValueIds(@Param("attrValueIds") Collection<Long> attrValueIds);

	/**
	 * 批量更新商品基本属性
	 *
	 * @param spuAttrValues
	 */
	void batchUpdateSpuAttrValue(@Param("spuAttrValues") List<SpuAttrValue> spuAttrValues);

	/**
	 * 根据商品id及属性id列表, 批量删除商品属性信息
	 *
	 * @param spuId
	 * @param attrIds
	 */
    void deleteBatchBySpuIdAndAttrIds(@Param("spuId") Long spuId, @Param("attrIds") List<Long> attrIds);

    List<SpuAttrValueAppVO> getSpuAttrAppBySpuId(@Param("spuId") Long spuId);
}
