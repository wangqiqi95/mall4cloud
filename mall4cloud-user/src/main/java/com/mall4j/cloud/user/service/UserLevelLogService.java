package com.mall4j.cloud.user.service;

import com.mall4j.cloud.api.user.bo.BuyVipNotifyBO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.user.model.UserLevelLog;

import java.util.List;

/**
 * 用户等级记录
 *
 * @author FrozenWatermelon
 * @date 2021-05-14 11:04:52
 */
public interface UserLevelLogService {

	/**
	 * 分页获取用户等级记录列表
	 * @param pageDTO 分页参数
	 * @return 用户等级记录列表分页数据
	 */
	PageVO<UserLevelLog> page(PageDTO pageDTO);

	/**
	 * 根据用户等级记录id获取用户等级记录
	 *
	 * @param levelLogId 用户等级记录id
	 * @return 用户等级记录
	 */
	UserLevelLog getByLevelLogId(Long levelLogId);

	/**
	 * 保存用户等级记录
	 * @param userLevelLog 用户等级记录
	 */
	void save(UserLevelLog userLevelLog);

	/**
	 * 更新用户等级记录
	 * @param userLevelLog 用户等级记录
	 */
	void update(UserLevelLog userLevelLog);

	/**
	 * 根据用户等级记录id删除用户等级记录
	 * @param levelLogId 用户等级记录id
	 */
	void deleteById(Long levelLogId);

	/**
	 * 购买vip成功
	 *
	 * @param userLevelLog 日志信息
	 * @param message 消息
	 */
    void paySuccess(UserLevelLog userLevelLog, BuyVipNotifyBO message);

	/**
	 * 批量保存用户等级变更日志
	 * @param userLevelLogs 保存日志列表
	 * @return 影响行数
	 */
    int batchSaveUserLevelLogs(List<UserLevelLog> userLevelLogs);

	/**
	 * 获取用户最高的等级（普通会员）
	 * @param userId 用户id
	 * @return 用户等级
	 */
	Integer getMaxLevelByUserId(Long userId);
}
