package com.mall4j.cloud.payment.mapper;

import com.mall4j.cloud.payment.model.RefundInfo;
import com.mall4j.cloud.payment.vo.AccountDetailVO;
import com.mall4j.cloud.payment.vo.RefundInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 退款信息
 *
 * @author FrozenWatermelon
 * @date 2021-03-15 15:26:03
 */
public interface RefundInfoMapper {

	/**
	 * 获取退款信息列表
	 * @return 退款信息列表
	 */
	List<RefundInfo> list();

	/**
	 * 根据退款信息id获取退款信息
	 *
	 * @param refundId 退款信息id
	 * @return 退款信息
	 */
	RefundInfo getByRefundId(@Param("refundId") Long refundId);

	RefundInfo getByRefundNumber(@Param("refundNumber") String refundNumber);

	/**
	 * 保存退款信息
	 * @param refundInfo 退款信息
	 */
	void save(@Param("refundInfo") RefundInfo refundInfo);

	/**
	 * 更新退款信息
	 * @param refundInfo 退款信息
	 */
	void update(@Param("refundInfo") RefundInfo refundInfo);

	/**
	 * 根据退款信息id删除退款信息
	 * @param refundId 退款单号
	 */
	void deleteById(@Param("refundId") Long refundId);

	/**
	 * 计算执行退款单的数量
	 *
	 * @param refundId 退款单号
	 * @return 执行退款单的数量
	 */
	int countByRefundId(@Param("refundId") Long refundId);

	/**
	 * 根据时间参数获取退款账户详情
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return 退款账户详情
	 */
	AccountDetailVO getRefundAccountDetail(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

	/**
	 * 根据时间参数获取退款详情
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return 退款详情
	 */
	List<RefundInfoVO> getRefundInfoVO(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
