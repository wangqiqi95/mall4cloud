package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.model.DistributionStoreActivitySubscribe;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 门店活动-用户订阅
 *
 * @author gww
 * @date 2022-01-28 23:24:49
 */
public interface DistributionStoreActivitySubscribeMapper {

	/**
	 * 通过会员id获取门店活动-用户订阅记录
	 *
	 * @param userId 会员id
	 * @return
	 */
	DistributionStoreActivitySubscribe getByUserId(@Param("userId") Long userId);

	/**
	 * 保存门店活动-用户订阅
	 *
	 * @param distributionStoreActivitySubscribe 门店活动-用户订阅
	 */
	void save(@Param("distributionStoreActivitySubscribe") DistributionStoreActivitySubscribe distributionStoreActivitySubscribe);

	/**
	 * 更新门店活动-用户订阅
	 * @param userId 会员id
	 */
	void cancelSubscribe(@Param("userId") Long userId);
}
