package com.mall4j.cloud.user.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.user.dto.UserAdminDTO;
import com.mall4j.cloud.user.dto.UserRechargeDTO;
import com.mall4j.cloud.user.model.UserRecharge;
import com.mall4j.cloud.user.vo.UserRechargeVO;

import java.util.List;

/**
 * 余额充值级别表
 *
 * @author FrozenWatermelon
 * @date 2021-04-27 15:51:36
 */
public interface UserRechargeService {

	/**
	 * 分页获取余额充值级别表列表
	 * @param pageDTO 分页参数
	 * @return 余额充值级别表列表分页数据
	 */
	PageVO<UserRecharge> page(PageDTO pageDTO);

	/**
	 * 根据余额充值级别表id获取余额充值级别表
	 *
	 * @param rechargeId 余额充值级别表id
	 * @return 余额充值级别表
	 */
	UserRechargeVO getRechargeInfo(Long rechargeId);

	/**
	 * 保存余额充值级别表
	 * @param userRecharge 余额充值级别表
	 */
	void save(UserRecharge userRecharge);

	/**
	 * 更新余额充值级别表
	 * @param userRecharge 余额充值级别表
	 */
	void update(UserRecharge userRecharge);

	/**
	 * 根据余额充值级别表id删除余额充值级别表
	 * @param rechargeId 余额充值级别表id
	 */
	void deleteById(Long rechargeId);

	/**
	 * 获取余额充值级别表列表
	 * @return
	 */
	List<UserRechargeVO> list();

	/**
	 * 清除缓存
	 * @param rechargeId
	 */
	void removeCacheByRechargeId(Long rechargeId);
	/**
	 * 批量修改用户余额
	 * @param userAdminDTO
	 * @return
	 */
	boolean batchUpdateUserBalance(UserAdminDTO userAdminDTO);

	/**
	 * 根据余额充值级别表id获取余额充值级别详情
	 * @param rechargeId
	 * @param putOnStatus
	 * @return
	 */
	UserRechargeVO getRechargeByRechargeId(Long rechargeId, Integer putOnStatus);

	/**
	 * 保存充值信息
	 * @param userRechargeDTO
	 * @return
	 */
    boolean saveRecharge(UserRechargeDTO userRechargeDTO);

	/**
	 * 修改充值信息
	 * @param userRechargeDTO
	 */
	void updateByRechargeId(UserRechargeDTO userRechargeDTO);

}
