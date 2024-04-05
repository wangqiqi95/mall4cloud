package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.OrderSettlement;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单结算表
 *
 * @author FrozenWatermelon
 * @date 2022-04-04 23:32:09
 */
public interface OrderSettlementMapper {

	/**
	 * 获取订单结算表列表
	 * @return 订单结算表列表
	 */
	List<OrderSettlement> list();

	/**
	 * 根据订单结算表id获取订单结算表
	 *
	 * @param settlementId 订单结算表id
	 * @return 订单结算表
	 */
	OrderSettlement getBySettlementId(@Param("settlementId") Long settlementId);

    OrderSettlement getByOrderId(@Param("orderId") Long orderId);
    OrderSettlement getByOrderId2(@Param("orderId") Long orderId);

	/**
	 * 保存订单结算表
	 * @param orderSettlement 订单结算表
	 */
	void save(@Param("orderSettlement") OrderSettlement orderSettlement);

	/**
	 * 更新订单结算表
	 * @param orderSettlement 订单结算表
	 */
	void update(@Param("orderSettlement") OrderSettlement orderSettlement);

	/**
	 * 根据订单结算表id删除订单结算表
	 * @param settlementId
	 */
	void deleteById(@Param("settlementId") Long settlementId);
}
