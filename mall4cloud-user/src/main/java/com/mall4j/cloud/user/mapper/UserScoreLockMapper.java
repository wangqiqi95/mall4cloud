package com.mall4j.cloud.user.mapper;

import com.mall4j.cloud.user.model.UserScoreLock;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 积分锁定信息
 *
 * @author FrozenWatermelon
 * @date 2021-05-19 19:54:55
 */
public interface UserScoreLockMapper {

	/**
	 * 获取积分锁定信息列表
	 *
	 * @return 积分锁定信息列表
	 */
	List<UserScoreLock> list();

	/**
	 * 根据积分锁定信息id获取积分锁定信息
	 *
	 * @param id 积分锁定信息id
	 * @return 积分锁定信息
	 */
	UserScoreLock getById(@Param("id") Long id);

	/**
	 * 保存积分锁定信息
	 *
	 * @param userScoreLock 积分锁定信息
	 */
	void save(@Param("userScoreLock") UserScoreLock userScoreLock);

	/**
	 * 更新积分锁定信息
	 *
	 * @param userScoreLock 积分锁定信息
	 */
	void update(@Param("userScoreLock") UserScoreLock userScoreLock);

	/**
	 * 根据积分锁定信息id删除积分锁定信息
	 *
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	/**
	 * 批量保存锁定积分
	 *
	 * @param userScoreLocks 锁定积分记录
	 */
    void saveBatch(@Param("userScoreLocks") List<UserScoreLock> userScoreLocks);

	/**
	 * 获取需要解锁的用户积分id列表
	 *
	 * @param orderId 订单id
	 * @return 用户的积分id列表
	 */
	List<UserScoreLock> listUserScoreLocksByOrderId(Long orderId);

	/**
	 * 将锁定状态标记为已解锁
	 *
	 * @param status
	 * @param userScoreLockIds 用户锁定的ids
	 * @return
	 */
	int unLockByIds(@Param("status") Integer status, @Param("userScoreLockIds") List<Long> userScoreLockIds);
}
