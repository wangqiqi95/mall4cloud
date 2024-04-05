package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.distribution.model.DistributionStoreActivitySubscribe;

/**
 * 门店活动-用户订阅
 *
 * @author gww
 * @date 2022-01-28 23:24:49
 */
public interface DistributionStoreActivitySubscribeService {


	/**
	 * 通过会员id获取门店活动-用户订阅记录
	 * @param userId 会员id
	 * @return
	 */
	DistributionStoreActivitySubscribe getByUserId(Long userId);

	/**
	 * 门店活动-订阅
	 * @param userId 会员id
	 */
	void subscribe(Long userId);

	/**
	 * 门店活动-取消订阅
	 * @param userId 会员id
	 */
	void cancelSubscribe(Long userId);

}
