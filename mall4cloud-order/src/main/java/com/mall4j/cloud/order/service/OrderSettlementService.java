package com.mall4j.cloud.order.service;

import com.mall4j.cloud.api.order.vo.OrderSettlementSimpleVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.order.model.OrderSettlement;
import com.mall4j.cloud.order.vo.OrderSettlementVO;

import java.util.List;

/**
 * 订单结算表
 *
 * @author FrozenWatermelon
 * @date 2020-12-05 14:13:50
 */
public interface OrderSettlementService {

	/**
	 * 分页获取订单结算表列表
	 * @param pageDTO 分页参数
	 * @return 订单结算表列表分页数据
	 */
	PageVO<OrderSettlementVO> page(PageDTO pageDTO);

	/**
	 * 根据订单结算表id获取订单结算表
	 *
	 * @param settlementId 订单结算表id
	 * @return 订单结算表
	 */
	OrderSettlementVO getBySettlementId(Long settlementId);

	/**
	 * 更新订单结算表
	 * @param orderSettlement 订单结算表
	 */
	void update(OrderSettlement orderSettlement);

	/**
	 * 根据订单结算表id删除订单结算表
	 * @param settlementId
	 */
	void deleteById(Long settlementId);

	/**
	 * 根据订单id获取订单结算信息
	 * @param orderId
	 * @return
	 */
    OrderSettlement getByOrderId(Long orderId);

	/**
	 * 根据支付单号列表获取订单号列表
	 * @param payIds
	 * @return
	 */
	List<OrderSettlementSimpleVO> listOrderIdsByPayIds(List<Long> payIds);
}
