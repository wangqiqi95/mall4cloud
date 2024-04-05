package com.mall4j.cloud.user.mapper;

import cn.hutool.core.date.DateTime;
import com.mall4j.cloud.user.model.UserScoreGetLog;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 用户积分获取记录
 *
 * @author FrozenWatermelon
 * @date 2021-05-17 17:17:14
 */
public interface UserScoreGetLogMapper {

	/**
	 * 获取用户积分获取记录列表
	 *
	 * @return 用户积分获取记录列表
	 */
	List<UserScoreGetLog> list();

	/**
	 * 根据用户积分获取记录id获取用户积分获取记录
	 *
	 * @param userScoreGetLogId 用户积分获取记录id
	 * @return 用户积分获取记录
	 */
	UserScoreGetLog getByUserScoreGetLogId(@Param("userScoreGetLogId") Long userScoreGetLogId);

	/**
	 * 保存用户积分获取记录
	 *
	 * @param userScoreGetLog 用户积分获取记录
	 */
	void save(@Param("userScoreGetLog") UserScoreGetLog userScoreGetLog);

	/**
	 * 更新用户积分获取记录
	 *
	 * @param userScoreGetLog 用户积分获取记录
	 */
	void update(@Param("userScoreGetLog") UserScoreGetLog userScoreGetLog);

	/**
	 * 根据用户积分获取记录id删除用户积分获取记录
	 *
	 * @param userScoreGetLogId
	 */
	void deleteById(@Param("userScoreGetLogId") Long userScoreGetLogId);

	/**
	 * 批量保存积分明细
	 *
	 * @param userScoreGetLogs
	 * @return
	 */
    void saveBatch(@Param("userScoreGetLogs") List<UserScoreGetLog> userScoreGetLogs);

	/**
	 * 根据用户id查询最近一条积分过期记录
	 * @param userId 用户id
	 * @return 积分过期记录
	 */
	UserScoreGetLog getLogByUserId(@Param("userId") Long userId);

	/**
	 * 查询已经过期但还没标记的积分
	 * @param userId 用户id
	 * @param expireTime 过期时间
	 * @param status 状态
	 * @return 用户积分记录列表
	 */
	List<UserScoreGetLog> listByUserIdAndExpireTimeAndStatus(@Param("userId") Long userId, @Param("expireTime") Date expireTime, @Param("status") Integer status);

	/**
	 * 更新用户积分记录
	 * @param userScoreGetLogList 用户积分记录列表
	 */
	void updateBatchById(@Param("userScoreGetLogList") List<UserScoreGetLog> userScoreGetLogList);

	/**
	 * 查询用户可用积分
	 * @param userId 用户id
	 * @param status 状态
	 * @param current 开始搜索的索引
	 * @param size 分页的大小
	 * @return 用户可用积分
	 */
	Long sumUsableScoreByPage(@Param("userId") Long userId, @Param("status") Integer status, @Param("current") Integer current, @Param("size") Integer size);

	/**
	 * 查询用户积分详细表数据
	 * @param userId 用户id
	 * @param status 状态
	 * @return 用户积分详细表数据
	 */
	List<UserScoreGetLog> listByCreateTime(@Param("userId") Long userId, @Param("status") Integer status);

	/**
	 * 查询用户积分记录列表
	 * @param userId 用户id
	 * @param status 状态
	 * @param current 开始搜索的索引
	 * @param size 分页的大小
	 * @return
	 */
	List<UserScoreGetLog> listByCreateTimeAndPage(@Param("userId") Long userId, @Param("status") Integer status, @Param("current") Integer current, @Param("size") Integer size);

	/**
	 * 批量更新用户积分状态
	 *
	 * @param status        状态
	 * @param userScoreGetIds 用户积分ids
	 */
	void batchUpdateUserScoreGetStatus(@Param("status") Integer status, @Param("userScoreGetIds") List<Long> userScoreGetIds);

	/**
	 * 获取用户可用的积分明细
	 * @param userIds 用户id集合
	 * @param status 状态 ScoreGetLogStatusEnum
	 * @return 用户可用的积分明细
	 */
	List<UserScoreGetLog> batchListByCreateTime(@Param("userIds") List<Long> userIds, @Param("status") Integer status);

	/**
	 * 修改状态为0的积分明细为过期状态
	 * @param expireTime 过期时间
	 */
    void updateExpireScoreDetail(@Param("expireTime") DateTime expireTime);

	/**
	 * 修改状态为0的积分明细为过期状态
	 * @param expireTime 过期时间
	 * @return
	 */
	List<UserScoreGetLog> listExpireScoreDetail(@Param("expireTime") DateTime expireTime);

	/**
	 * 根据订单id查询最近一条积分过期记录
	 *
	 * @param bizId 订单id
	 * @return 积分过期记录
	 */
	UserScoreGetLog getLogByBizId(@Param("bizId") Long bizId);
}
