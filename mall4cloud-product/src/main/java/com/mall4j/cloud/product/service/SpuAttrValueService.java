package com.mall4j.cloud.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.common.product.dto.SpuAttrValueDTO;
import com.mall4j.cloud.common.product.vo.SpuAttrValueLangVO;
import com.mall4j.cloud.common.product.vo.app.SpuAttrValueAppVO;
import com.mall4j.cloud.product.model.SpuAttrValue;
import com.mall4j.cloud.common.product.vo.SpuAttrValueVO;

import java.util.List;
import java.util.Set;

/**
 * 商品规格属性关联信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
public interface SpuAttrValueService extends IService<SpuAttrValue> {

	/**
	 * 更新商品规格属性关联信息
	 *
	 * @param spuId           id
	 * @param categoryId      分类id
	 * @param spuAttrValues   商品属性信息
	 */
	void update(Long spuId, Long categoryId, List<SpuAttrValueDTO> spuAttrValues);


	/**
	 * 根据商品id删除spuId
	 *
	 * @param spuId id
	 */
	void deleteBySpuId(Long spuId);

	/**
	 * 根据属性和属性值id列表删除商品属性关联信息, 并发送消息到队列（更新对应的spu信息）
	 * @param attrId
	 * @param attrValueId
	 */
    void deleteUnionDataByAttId(Long attrId, List<Long> attrValueId);

	/**
	 * 根据属性和分类id列表删除商品属性关联信息, 并发送消息到队列（更新对应的spu信息）
	 * @param attrId
	 * @param categoryIds
	 */
    void deleteByCategoryIds(Long attrId, List<Long> categoryIds);

	/**
	 * 根据spuId获取商品属性列表
	 * @param spuId
	 * @return
	 */
	List<SpuAttrValueVO> getSpuAttrsBySpuId(Long spuId);

	/**
	 * 保存商品属性信息
	 * @param spuId
	 * @param categoryId
	 * @param spuAttrValues
	 */
	void save(Long spuId, Long categoryId, List<SpuAttrValueDTO> spuAttrValues);


//	/**
//	 * 获取商品详情页面的商品属性列表
//	 * @param spuId 商品id
//	 * @return 商品属性列表
//	 */
//	List<SpuAttrValueLangVO> spuAttrValueListBySpuId(Long spuId);

//	/**
//	 * 批量清除商品属性缓存
//	 * @param spuIds
//	 */
//	void removeSpuAttrValue(List<Long> spuIds);

	/**
	 * 更新es、redis中的商品属性信息
	 * @param attrValueList
	 * @param attrValueIds
	 */
    void updateSpuByAttrValueIds(List<SpuAttrValue> attrValueList, Set<Long> attrValueIds);

	List<SpuAttrValueAppVO> getSpuAttrValueAppVOBySpuId(Long spuId);

    Boolean updateSpuAttrValue(List<SpuAttrValueDTO> spuAttrValues, Long spuId);
}
