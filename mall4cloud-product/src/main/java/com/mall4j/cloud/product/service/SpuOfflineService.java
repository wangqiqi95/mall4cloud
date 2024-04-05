package com.mall4j.cloud.product.service;

import java.util.List;

/**
 * spu信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
public interface SpuOfflineService {

	/**
	 * 批量下线商品及关联的活动
	 * @param type 下线商品和活动类型，1.直接通过商品ids，2.通过shopId判断为平台还是店铺分类ids进行下架，3.下线店铺通过shopId
	 * @param spuIds 商品ids
	 * @param shopId 店铺id
	 * @param categoryIds 分类id
	 * @param status 修改后的商品状态，为空 默认查询条件status是1
	 */
	void offlineSpuStatusAndActivity(Integer type, List<Long> spuIds, Long shopId, List<Long> categoryIds, Integer status);

	/**
	 * 下线品牌相关商品和活动
	 * @param spuIds 商品ids
	 * @param brandId 品牌id
	 */
    void offlineSpuStatusAndActivityByBrandId(List<Long> spuIds, Long brandId);
}
