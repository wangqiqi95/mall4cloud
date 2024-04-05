package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单信息
 *
 * @author FrozenWatermelon
 * @date 2022-04-04 23:32:08
 */
public interface OrderMapper {

	/**
	 * 获取订单信息列表
	 * @return 订单信息列表
	 */
	List<Order> list();

	/**
	 * 根据订单信息id获取订单信息
	 *
	 * @param orderId 订单信息id
	 * @return 订单信息
	 */
	Order getByOrderId(@Param("orderId") Long orderId);

    Order getByOrderId2(@Param("orderId") Long orderId);

	/**
	 * 保存订单信息
	 * @param order 订单信息
	 */
	void save(@Param("order") Order order);

    /**
     * 批量保存订单数据
     *
     * @param orders
     */
    void saveBatch(@Param("orders") List<Order> orders);

	/**
	 * 更新订单信息
	 * @param order 订单信息
	 */
	void update(@Param("order") Order order);

	/**
	 * 根据订单信息id删除订单信息
	 * @param orderId
	 */
	void deleteById(@Param("orderId") Long orderId);
}
