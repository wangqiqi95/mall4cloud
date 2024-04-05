package com.mall4j.cloud.delivery.mapper;

import com.mall4j.cloud.delivery.model.DeliveryOrderItem;
import com.mall4j.cloud.delivery.vo.DeliveryOrderItemVO;
import com.mall4j.cloud.delivery.vo.DeliveryOrderVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 物流订单项信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:00
 */
public interface DeliveryOrderItemMapper {

	/**
	 * 获取物流订单项信息列表
	 *
	 * @return 物流订单项信息列表
	 */
	List<DeliveryOrderItemVO> list();

	/**
	 * 根据物流订单项信息id获取物流订单项信息
	 *
	 * @param id 物流订单项信息id
	 * @return 物流订单项信息
	 */
	DeliveryOrderItemVO getById(@Param("id") Long id);

	/**
	 * 保存物流订单项信息
	 *
	 * @param deliveryOrderItem 物流订单项信息
	 */
	void save(@Param("deliveryOrderItem") DeliveryOrderItem deliveryOrderItem);

	/**
	 * 更新物流订单项信息
	 *
	 * @param deliveryOrderItem 物流订单项信息
	 */
	void update(@Param("deliveryOrderItem") DeliveryOrderItem deliveryOrderItem);

	/**
	 * 根据物流订单项信息id删除物流订单项信息
	 *
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	/**
	 * 批量保存订单项物流
	 *
	 * @param deliveryOrderItems
	 */
    void saveBatch(@Param("deliveryOrderItems") List<DeliveryOrderItem> deliveryOrderItems);

	/**
	 * 查询订单物流包裹信息
	 *
	 * @param orderId
	 * @return
	 */
	List<DeliveryOrderVO> listDetailDelivery(@Param("orderId") Long orderId);
}
