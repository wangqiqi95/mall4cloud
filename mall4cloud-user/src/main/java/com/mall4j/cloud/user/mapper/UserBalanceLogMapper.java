package com.mall4j.cloud.user.mapper;

import com.mall4j.cloud.api.user.dto.MemberReqDTO;
import com.mall4j.cloud.common.database.util.PageAdapter;
import com.mall4j.cloud.user.model.UserBalanceLog;
import com.mall4j.cloud.user.vo.UserBalanceLogVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 余额记录
 *
 * @author FrozenWatermelon
 * @date 2021-04-27 15:51:36
 */
public interface UserBalanceLogMapper {

	/**
	 * 获取余额记录列表
	 *
	 * @return 余额记录列表
	 */
	List<UserBalanceLog> list();

	/**
	 * 根据余额记录id获取余额记录
	 *
	 * @param balanceLogId 余额记录id
	 * @return 余额记录
	 */
	UserBalanceLog getByBalanceLogId(@Param("balanceLogId") Long balanceLogId);

	/**
	 * 保存余额记录
	 *
	 * @param userBalanceLog 余额记录
	 */
	void save(@Param("userBalanceLog") UserBalanceLog userBalanceLog);

	/**
	 * 更新余额记录
	 *
	 * @param userBalanceLog 余额记录
	 */
	void update(@Param("userBalanceLog") UserBalanceLog userBalanceLog);

	/**
	 * 根据余额记录id删除余额记录
	 *
	 * @param balanceLogId
	 */
	void deleteById(@Param("balanceLogId") Long balanceLogId);

	/**
	 * 分页查询某个用户的余额明细
	 *
	 * @param pageAdapter
	 * @param userId
	 * @return
	 */
	List<UserBalanceLogVO> listByUserId(@Param("page") PageAdapter pageAdapter, @Param("userId") Long userId);

	/**
	 * 统计某个用户的余额明细的总条数
	 *
	 * @param userId
	 * @return
	 */
	Long countByUserId(@Param("userId") Long userId);

	/**
	 * 批量保存用户余额日志
	 *
	 * @param userBalanceLogs
	 * @return
	 */
	Long saveBatch(@Param("userBalanceLogs") List<UserBalanceLog> userBalanceLogs);

	/**
	 * 更新为支付成功
	 *
	 * @param balanceLogId 充值记录id
	 * @param payId        支付id
	 * @return 是否更新成功
	 */
    int updateToSuccess(@Param("balanceLogId") Long balanceLogId, @Param("payId") Long payId);

	/**
	 * 通过支付单号获取余额记录
	 *
	 * @param payId 支付单号
	 * @return 余额记录
	 */
	UserBalanceLog getByPayId(@Param("payId") Long payId);

	/**
	 * 根据充值金额，获取用户id列表
	 *
	 * @param isPayed   是否支付
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @param minAmount 最小金额
	 * @param maxAmount 最大金额
	 * @return 用户id列表
	 */
    List<Long> listUserIdByRechargeAmount(@Param("isPayed") Integer isPayed,
										  @Param("startDate") Date startDate,
										  @Param("endDate") Date endDate,
										  @Param("minAmount") Long minAmount,
										  @Param("maxAmount") Long maxAmount);

	/**
	 * 根据充值次数，获取用户id列表
	 *
	 * @param isPayed   是否支付
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @param minNum    最小次数
	 * @param maxNum    最大次数
	 * @return 用户id列表
	 */
	List<Long> listUserIdByRechargeNum(@Param("isPayed") Integer isPayed,
									   @Param("startDate") Date startDate,
									   @Param("endDate") Date endDate,
									   @Param("minNum") Long minNum,
									   @Param("maxNum") Long maxNum);

	/**
	 * 根据退款订单id获取退款的数量
	 *
	 * @param refundId 退款id
	 * @return 退款数量
	 */
    int countByRefundId(@Param("refundId") Long refundId);


	/**
	 * --------------数据分析相关sql--------------
	 * 根据条件参数，统计会员数量
	 * @param param 参数
	 * @return 数量
	 */
    Integer countByConditions(@Param("param") MemberReqDTO param);
}
