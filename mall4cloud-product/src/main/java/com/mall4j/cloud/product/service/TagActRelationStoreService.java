package com.mall4j.cloud.product.service;

import com.mall4j.cloud.product.model.TagActRelationProd;
import com.mall4j.cloud.product.model.TagActRelationStore;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 营销活动标签关联商店表
 * @author hwy
 * @date 2022-03-12 14:28:10
 */
public interface TagActRelationStoreService {

	/**
	 * 根据标签名称分页获取营销活动标签关联商店表列表
	 * @param actId  活动id
	 * @return 营销活动标签关联商店表列表分页数据
	 */
	List<TagActRelationStore> listByActId(@Param("actId") Long actId);

	List<TagActRelationStore> listsByActId(Long actId);


	/**
	 * 保存营销活动标签关联商店表
	 * @param tagActRelationStore 营销活动标签关联商店表
	 */
	void save(@Param("et") TagActRelationStore tagActRelationStore);

	/**
	 * 根据营销活动标签关联商店表id删除营销活动标签关联商店表
	 * @param actId 活动id
	 * @param storeId 商品id
	 */
	void deleteById(@Param("actId") Long actId,@Param("storeId") Long storeId);

	/**
	 * 根据活动id删除
	 * @param actId 活动id
	 */
	void deleteByActId(@Param("actId") Long actId);

}
