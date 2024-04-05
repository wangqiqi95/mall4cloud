package com.mall4j.cloud.order.mapper;

import com.mall4j.cloud.api.order.vo.OrderSettlementSimpleVO;
import com.mall4j.cloud.order.model.OrderSettlement;
import com.mall4j.cloud.order.vo.OrderSettlementVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单结算表
 *
 * @author FrozenWatermelon
 * @date 2020-12-05 14:13:50
 */
public interface OrderSettlementMapper {

	/**
	 * 获取订单结算表列表
	 *
	 * @return 订单结算表列表
	 */
	List<OrderSettlementVO> list();

	/**
	 * 根据订单结算表id获取订单结算表
	 *
	 * @param settlementId 订单结算表id
	 * @return 订单结算表
	 */
	OrderSettlementVO getBySettlementId(@Param("settlementId") Long settlementId);

	/**
	 * 更新订单结算表
	 *
	 * @param orderSettlement 订单结算表
	 */
	void update(@Param("orderSettlement") OrderSettlement orderSettlement);

	/**
	 * 根据订单结算表id删除订单结算表
	 *
	 * @param settlementId
	 */
	void deleteById(@Param("settlementId") Long settlementId);

	/**
	 * 批量保存
	 *
	 * @param orderSettlements 结算单列表
	 */
    void saveBatch(@Param("orderSettlements") List<OrderSettlement> orderSettlements);

	/**
	 * 获取订单支付信息
	 *
	 * @param orderId
	 * @return
	 */
	OrderSettlement getByOrderId(@Param("orderId") Long orderId);

	/**
	 * 更新为已支付状态
	 *
	 * @param orderIds
	 * @param payId
	 * @param payType
	 */
    void updateToPaySuccess(@Param("orderIds") List<Long> orderIds, @Param("payId") Long payId, @Param("payType") Integer payType);

	/**
	 * 根据支付单号列表获取订单号列表
	 * @param payIds
	 * @return
	 */
	List<OrderSettlementSimpleVO> listOrderIdsByPayIds(@Param("payIds") List<Long> payIds);
}
