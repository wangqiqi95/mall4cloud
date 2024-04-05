package com.mall4j.cloud.user.service;

import com.mall4j.cloud.api.user.bo.BalanceRefundBO;
import com.mall4j.cloud.api.user.bo.RechargeNotifyBO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.user.model.UserBalanceLog;
import com.mall4j.cloud.user.vo.UserBalanceLogVO;

import java.util.List;

/**
 * 余额记录
 *
 * @author FrozenWatermelon
 * @date 2021-04-27 15:51:36
 */
public interface UserBalanceLogService {

	/**
	 * 分页获取余额记录列表
	 * @param pageDTO 分页参数
	 * @return 余额记录列表分页数据
	 */
	PageVO<UserBalanceLog> page(PageDTO pageDTO);

	/**
	 * 根据余额记录id获取余额记录
	 *
	 * @param balanceLogId 余额记录id
	 * @return 余额记录
	 */
	UserBalanceLog getByBalanceLogId(Long balanceLogId);

	/**
	 * 保存余额记录
	 * @param userBalanceLog 余额记录
	 */
	void save(UserBalanceLog userBalanceLog);

	/**
	 * 更新余额记录
	 * @param userBalanceLog 余额记录
	 */
	void update(UserBalanceLog userBalanceLog);

	/**
	 * 根据余额记录id删除余额记录
	 * @param balanceLogId 余额记录id
	 */
	void deleteById(Long balanceLogId);
	/**
	 * 分页查询某个用户的余额明细
	 * @param pageDTO
	 * @param userId
	 * @return
	 */
    PageVO<UserBalanceLogVO> getPageByUserId(PageDTO pageDTO, Long userId);

	/**
	 * 批量保存用户余额日志
	 * @param userRechargeLogs
	 * @return 受影响行数
	 */
	Long saveBatch(List<UserBalanceLog> userRechargeLogs);

	/**
	 * 充值成功
	 * @param message
	 */
    void rechargeSuccess(RechargeNotifyBO message);

	/**
	 * 通过支付单号获取余额记录
	 *
	 * @param payId 支付单号
	 * @return 余额记录
	 */
	UserBalanceLog getByPayId(Long payId);

	/**
	 * 扣减余额，并将余额记录的支付状态变为已支付
	 * @param payId 支付单号
	 * @param userBalanceLog 余额记录
	 */
    void updateToOrderPaySuccess(Long payId, UserBalanceLog userBalanceLog);

	/**
	 * 执行退款
	 * @param balanceRefundBO 退款参数
	 */
	void doRefund(BalanceRefundBO balanceRefundBO);
}
