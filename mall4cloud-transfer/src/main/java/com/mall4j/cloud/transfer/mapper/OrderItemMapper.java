package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单项
 *
 * @author FrozenWatermelon
 * @date 2022-04-04 23:32:08
 */
public interface OrderItemMapper {

	/**
	 * 获取订单项列表
	 * @return 订单项列表
	 */
	List<OrderItem> list();

	/**
	 * 根据订单项id获取订单项
	 *
	 * @param orderItemId 订单项id
	 * @return 订单项
	 */
	OrderItem getByOrderItemId(@Param("orderItemId") Long orderItemId);
    OrderItem getByOrderItemId2(@Param("orderItemId") Long orderItemId);

    List<OrderItem> getByOrderId(@Param("orderId") Long orderId);

	/**
	 * 保存订单项
	 * @param orderItem 订单项
	 */
	void save(@Param("orderItem") OrderItem orderItem);

	/**
	 * 更新订单项
	 * @param orderItem 订单项
	 */
	void update(@Param("orderItem") OrderItem orderItem);

	/**
	 * 根据订单项id删除订单项
	 * @param orderItemId
	 */
	void deleteById(@Param("orderItemId") Long orderItemId);
}
