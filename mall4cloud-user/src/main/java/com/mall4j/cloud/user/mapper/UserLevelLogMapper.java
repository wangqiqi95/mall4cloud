package com.mall4j.cloud.user.mapper;

import com.mall4j.cloud.user.model.UserLevelLog;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 用户等级记录
 *
 * @author FrozenWatermelon
 * @date 2021-05-14 11:04:52
 */
public interface UserLevelLogMapper {

	/**
	 * 获取用户等级记录列表
	 *
	 * @return 用户等级记录列表
	 */
	List<UserLevelLog> list();

	/**
	 * 根据用户等级记录id获取用户等级记录
	 *
	 * @param levelLogId 用户等级记录id
	 * @return 用户等级记录
	 */
	UserLevelLog getByLevelLogId(@Param("levelLogId") Long levelLogId);

	/**
	 * 保存用户等级记录
	 *
	 * @param userLevelLog 用户等级记录
	 */
	void save(@Param("userLevelLog") UserLevelLog userLevelLog);

	/**
	 * 更新用户等级记录
	 *
	 * @param userLevelLog 用户等级记录
	 */
	void update(@Param("userLevelLog") UserLevelLog userLevelLog);

	/**
	 * 根据用户等级记录id删除用户等级记录
	 *
	 * @param levelLogId
	 */
	void deleteById(@Param("levelLogId") Long levelLogId);

	/**
	 * 更新为支付成功状态
	 *
	 * @param levelLogId 日志id
	 * @param payId      支付id
	 * @return 影响行数
	 */
    int updateToSuccess(@Param("levelLogId") Long levelLogId, @Param("payId") Long payId);

	/**
	 * 批量保存用户等级变更日志
	 * @param userLevelLogs 保存日志列表
	 * @return 影响行数
	 */
    int batchSaveUserLevelLogs(@Param("userLevelLogs") List<UserLevelLog> userLevelLogs);

	/**
	 * 根据开始结束时间及支付状态，筛选用户
	 * @param isPayed 是否支付
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return 用户id列表
	 */
	List<Long> listUserIdByEarliestRechargeTime(@Param("isPayed") Integer isPayed,
												  @Param("startDate") Date startDate,
												  @Param("endDate") Date endDate);

	/**
	 * 获取用户最高的等级（普通会员）
	 * @param userId 用户id
	 * @return 用户等级
	 */
	Integer getMaxLevelByUserId(@Param("userId") Long userId);
}
