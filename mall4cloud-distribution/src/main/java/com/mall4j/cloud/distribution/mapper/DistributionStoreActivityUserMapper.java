package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.dto.DistributionStoreActivityUserQueryDTO;
import com.mall4j.cloud.distribution.model.DistributionStoreActivityUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 门店活动-报名用户
 *
 * @author gww
 * @date 2021-12-27 13:42:16
 */
public interface DistributionStoreActivityUserMapper {

	/**
	 * 获取门店活动-报名用户列表
	 * @param distributionStoreActivityUserQueryDTO 查询参数
	 * @return 门店活动-报名用户列表
	 */
	List<DistributionStoreActivityUser> list(@Param("queryDTO") DistributionStoreActivityUserQueryDTO distributionStoreActivityUserQueryDTO);

	/**
	 * 通过活动id和报名状态统计
	 * @param activityId
	 * @param status
	 * @return
	 */
	Integer countByActivityIdAndStatus(@Param("activityId") Long activityId,@Param("status") Integer status);

	/**
	 * 根据活动id-获取门店活动-报名用户列表
	 * @param activityIdList 活动id集合
	 * @return 门店活动-报名用户列表
	 */
	List<DistributionStoreActivityUser> listByActivityIdIdList(@Param("activityIdList") List<Long> activityIdList);

	/**
	 * 根据活动id-获取门店活动-报名用户列表
	 * @param userId 用户id
	 * @return 门店活动-报名用户列表
	 */
	List<DistributionStoreActivityUser> listByUserId(@Param("userId") Long userId);

	/**
	 * 根据门店活动-报名用户id获取门店活动-报名用户
	 *
	 * @param id 门店活动-报名用户id
	 * @return 门店活动-报名用户
	 */
	DistributionStoreActivityUser getById(@Param("id") Long id);

	/**
	 * 保存门店活动-报名用户
	 * @param distributionStoreActivityUser 门店活动-报名用户
	 */
	void save(@Param("distributionStoreActivityUser") DistributionStoreActivityUser distributionStoreActivityUser);

	/**
	 * 更新门店活动-报名用户
	 * @param distributionStoreActivityUser 门店活动-报名用户
	 */
	void update(@Param("distributionStoreActivityUser") DistributionStoreActivityUser distributionStoreActivityUser);

	/**
	 * 门店活动取消
	 * @param userId
	 * @param activityId
	 */
	void cancel(@Param("userId") Long userId, @Param("activityId") Long activityId);

	/**
	 * 门店活动签到
	 * @param userId
	 * @param activityId
	 */
	void sign(@Param("userId") Long userId, @Param("activityId") Long activityId);


	/**
	 * 通过用户id和活动id查询报名详情
	 * @param userId
	 * @param activityId
	 * @return
	 */
	DistributionStoreActivityUser findByUserIdAndActivityId(@Param("userId") Long userId, @Param("activityId") Long activityId);

	/**
	 * 通过活动id和手机号查询报名详情
	 * @param activityId
	 * @param userMobile
	 * @return
	 */
	DistributionStoreActivityUser findByActivityIdAndUserMobile(@Param("activityId") Long activityId, @Param("userMobile") String userMobile);

}
