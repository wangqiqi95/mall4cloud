package com.mall4j.cloud.user.service;

import com.mall4j.cloud.api.user.bo.UserScoreBO;
import com.mall4j.cloud.api.user.dto.UserScoreLockDTO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.user.model.UserScoreLock;

import java.util.List;

/**
 * 积分锁定信息
 *
 * @author FrozenWatermelon
 * @date 2021-05-19 19:54:55
 */
public interface UserScoreLockService {

	/**
	 * 分页获取积分锁定信息列表
	 * @param pageDTO 分页参数
	 * @return 积分锁定信息列表分页数据
	 */
	PageVO<UserScoreLock> page(PageDTO pageDTO);

	/**
	 * 根据积分锁定信息id获取积分锁定信息
	 *
	 * @param id 积分锁定信息id
	 * @return 积分锁定信息
	 */
	UserScoreLock getById(Long id);

	/**
	 * 保存积分锁定信息
	 * @param userScoreLock 积分锁定信息
	 */
	void save(UserScoreLock userScoreLock);

	/**
	 * 更新积分锁定信息
	 * @param userScoreLock 积分锁定信息
	 */
	void update(UserScoreLock userScoreLock);

	/**
	 * 根据积分锁定信息id删除积分锁定信息
	 * @param id 积分锁定信息id
	 */
	void deleteById(Long id);

	/**
	 * 按照订单号锁定积分
	 * @param userScoreLocks 订单号和积分的集合
	 */
    void lock(List<UserScoreLockDTO> userScoreLocks);

	/**
	 * 按照订单号解锁积分
	 * @param userScoreBo 解锁订单号集合
	 */
	void unlock(UserScoreBO userScoreBo);
}
