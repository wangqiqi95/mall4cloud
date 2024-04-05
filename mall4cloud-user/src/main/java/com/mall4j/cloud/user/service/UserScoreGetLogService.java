package com.mall4j.cloud.user.service;

import cn.hutool.core.date.DateTime;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.user.model.UserScoreGetLog;

import java.util.Date;
import java.util.List;

/**
 * 用户积分获取记录
 *
 * @author FrozenWatermelon
 * @date 2021-05-17 17:17:14
 */
public interface UserScoreGetLogService {

	/**
	 * 分页获取用户积分获取记录列表
	 * @param pageDTO 分页参数
	 * @return 用户积分获取记录列表分页数据
	 */
	PageVO<UserScoreGetLog> page(PageDTO pageDTO);

	/**
	 * 根据用户积分获取记录id获取用户积分获取记录
	 *
	 * @param userScoreGetLogId 用户积分获取记录id
	 * @return 用户积分获取记录
	 */
	UserScoreGetLog getByUserScoreGetLogId(Long userScoreGetLogId);

	/**
	 * 保存用户积分获取记录
	 * @param userScoreGetLog 用户积分获取记录
	 */
	void save(UserScoreGetLog userScoreGetLog);

	/**
	 * 更新用户积分获取记录
	 * @param userScoreGetLog 用户积分获取记录
	 */
	void update(UserScoreGetLog userScoreGetLog);

	/**
	 * 根据用户积分获取记录id删除用户积分获取记录
	 * @param userScoreGetLogId 用户积分获取记录id
	 */
	void deleteById(Long userScoreGetLogId);

	/**
	 * 批量保存积分明细
	 * @param userScoreGetLogs
	 * @return
	 */
    void saveBatch(List<UserScoreGetLog> userScoreGetLogs);

	/**
	 * 根据用户id查询最近一条积分过期记录
	 * @param userId 用户id
	 * @return 积分过期记录
	 */
	UserScoreGetLog getLogByUserId(Long userId);

	/**
	 * 查询已经过期但还没标记的积分
	 * @param userId 用户id
	 * @param expireTime 过期时间
	 * @param status 状态
	 * @return 用户积分记录列表
	 */
	List<UserScoreGetLog> listByUserIdAndExpireTimeAndStatus(Long userId, Date expireTime, Integer status);
	/**
	 * 更新用户积分记录
	 * @param userScoreGetLogList 用户积分记录列表
	 */
	void updateBatchById(List<UserScoreGetLog> userScoreGetLogList);
	/**
	 * 查询用户可用积分
	 * @param userId 用户id
	 * @param status 状态
	 * @param current 开始搜索的索引
	 * @param size 分页的大小
	 * @return 用户可用积分
	 */
	Long sumUsableScoreByPage(Long userId, Integer status, Integer current, Integer size);
	/**
	 * 查询用户积分详细表数据
	 * @param userId 用户id
	 * @param status 状态
	 * @return 用户积分详细表数据
	 */
	List<UserScoreGetLog> listByCreateTime(Long userId, Integer status);
	/**
	 * 查询用户积分记录列表
	 * @param userId 用户id
	 * @param status 状态
	 * @param current 开始搜索的索引
	 * @param size 分页的大小
	 * @return
	 */
	List<UserScoreGetLog> listByCreateTimeAndPage(Long userId, Integer status, Integer current, Integer size);

	/**
	 * 获取用户可用的积分明细
	 * @param userIds 用户id集合
	 * @param status 状态 ScoreGetLogStatusEnum
	 * @return 用户可用的积分明细
	 */
    List<UserScoreGetLog> batchListByCreateTime(List<Long> userIds, Integer status);

	/**
	 * 修改用户过期积分
	 * @param dateTime 过期时间
	 */
    void updateExpireScoreDetail(DateTime dateTime);

	/**
	 * 根据订单id查询最近一条积分过期记录
	 *
	 * @param bizId 订单id
	 * @return 积分过期记录
	 */
	UserScoreGetLog getLogByBizId(Long bizId);
}
