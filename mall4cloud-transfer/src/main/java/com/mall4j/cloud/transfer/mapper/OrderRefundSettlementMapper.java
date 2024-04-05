package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.OrderRefundSettlement;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 退款支付结算单据
 *
 * @author FrozenWatermelon
 * @date 2022-04-04 23:32:09
 */
public interface OrderRefundSettlementMapper {

	/**
	 * 获取退款支付结算单据列表
	 * @return 退款支付结算单据列表
	 */
	List<OrderRefundSettlement> list();

	/**
	 * 根据退款支付结算单据id获取退款支付结算单据
	 *
	 * @param settlementId 退款支付结算单据id
	 * @return 退款支付结算单据
	 */
	OrderRefundSettlement getBySettlementId(@Param("settlementId") Long settlementId);

	/**
	 * 保存退款支付结算单据
	 * @param orderRefundSettlement 退款支付结算单据
	 */
	void save(@Param("orderRefundSettlement") OrderRefundSettlement orderRefundSettlement);

	/**
	 * 更新退款支付结算单据
	 * @param orderRefundSettlement 退款支付结算单据
	 */
	void update(@Param("orderRefundSettlement") OrderRefundSettlement orderRefundSettlement);

	/**
	 * 根据退款支付结算单据id删除退款支付结算单据
	 * @param settlementId
	 */
	void deleteById(@Param("settlementId") Long settlementId);
}
