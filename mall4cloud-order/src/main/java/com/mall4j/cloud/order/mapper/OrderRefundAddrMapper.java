package com.mall4j.cloud.order.mapper;

import com.mall4j.cloud.order.model.OrderRefundAddr;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户退货物流地址
 *
 * @author FrozenWatermelon
 * @date 2021-03-09 13:44:31
 */
public interface OrderRefundAddrMapper {

	/**
	 * 获取用户退货物流地址列表
	 *
	 * @return 用户退货物流地址列表
	 */
	List<OrderRefundAddr> list();

	/**
	 * 根据用户退货物流地址id获取用户退货物流地址
	 *
	 * @param refundAddrId 用户退货物流地址id
	 * @return 用户退货物流地址
	 */
	OrderRefundAddr getByRefundAddrId(@Param("refundAddrId") Long refundAddrId);

	/**
	 * 保存用户退货物流地址
	 *
	 * @param orderRefundAddr 用户退货物流地址
	 */
	void save(@Param("orderRefundAddr") OrderRefundAddr orderRefundAddr);

	/**
	 * 更新用户退货物流地址
	 *
	 * @param orderRefundAddr 用户退货物流地址
	 */
	void update(@Param("orderRefundAddr") OrderRefundAddr orderRefundAddr);

	/**
	 * 根据用户退货物流地址id删除用户退货物流地址
	 *
	 * @param refundAddrId
	 */
	void deleteById(@Param("refundAddrId") Long refundAddrId);

	/**
	 * 根据退款订单id获取退款地址信息
	 *
	 * @param refundId
	 * @return
	 */
    OrderRefundAddr getByRefundId(@Param("refundId") Long refundId);
}
