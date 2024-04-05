package com.mall4j.cloud.order.mapper;

import com.mall4j.cloud.order.model.OrderRefundSettlement;
import com.mall4j.cloud.order.vo.OrderRefundSettlementVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 退款支付结算单据
 *
 * @author FrozenWatermelon
 * @date 2020-12-05 14:13:50
 */
public interface OrderRefundSettlementMapper {

	/**
	 * 获取退款支付结算单据列表
	 *
	 * @return 退款支付结算单据列表
	 */
	List<OrderRefundSettlementVO> list();

	/**
	 * 根据退款支付结算单据id获取退款支付结算单据
	 *
	 * @param settlementId 退款支付结算单据id
	 * @return 退款支付结算单据
	 */
	OrderRefundSettlementVO getBySettlementId(@Param("settlementId") Long settlementId);

	/**
	 * 保存退款支付结算单据
	 *
	 * @param orderRefundSettlement 退款支付结算单据
	 */
	void save(@Param("orderRefundSettlement") OrderRefundSettlement orderRefundSettlement);

	/**
	 * 更新退款支付结算单据
	 *
	 * @param orderRefundSettlement 退款支付结算单据
	 */
	void update(@Param("orderRefundSettlement") OrderRefundSettlement orderRefundSettlement);

	/**
	 * 根据退款支付结算单据id删除退款支付结算单据
	 *
	 * @param settlementId
	 */
	void deleteById(@Param("settlementId") Long settlementId);

	/**
	 * 根据订单号获取统一退款订单数量
	 *
	 * @param refundId
	 * @return
	 */
    int countByRefundId(@Param("refundId") Long refundId);

	/**
	 * 根据退款单号更新结算单为已退款状态
	 *
	 * @param refundId
	 * @return 是否更新成功
	 */
	int updateToSuccessByRefundId(@Param("refundId") Long refundId);
}
