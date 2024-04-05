package com.mall4j.cloud.delivery.mapper;

import com.mall4j.cloud.api.delivery.dto.DeliveryOrderDTO;
import com.mall4j.cloud.api.delivery.vo.DeliveryOrderFeignVO;
import com.mall4j.cloud.delivery.model.DeliveryOrder;
import com.mall4j.cloud.delivery.vo.DeliveryOrderVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单快递信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:00
 */
public interface DeliveryOrderMapper {

	/**
	 * 获取订单快递信息列表
	 *
	 * @return 订单快递信息列表
	 */
	List<DeliveryOrderVO> list();

	/**
	 * 根据订单快递信息id获取订单快递信息
	 *
	 * @param deliveryOrderId 订单快递信息id
	 * @return 订单快递信息
	 */
	DeliveryOrderVO getByDeliveryOrderId(@Param("deliveryOrderId") Long deliveryOrderId);

	/**
	 * 保存订单快递信息
	 *
	 * @param deliveryOrder 订单快递信息
	 */
	void save(@Param("deliveryOrder") DeliveryOrder deliveryOrder);

	/**
	 * 更新订单快递信息
	 *
	 * @param deliveryOrder 订单快递信息
	 */
	void update(@Param("deliveryOrder") DeliveryOrder deliveryOrder);

	/**
	 * 根据订单快递信息id删除订单快递信息
	 *
	 * @param deliveryOrderId
	 */
	void deleteById(@Param("deliveryOrderId") Long deliveryOrderId);

	/**
	 * 根据订单号获取包裹信息
	 *
	 * @param orderId 订单id
	 * @return
	 */
	List<DeliveryOrderFeignVO> getByDeliveryByOrderId(@Param("orderId") Long orderId);

	/**
	 * 批量更新订单包裹信息
	 *
	 * @param deliveryOrderList
	 */
    void updateBatch(@Param("deliveryOrderList") List<DeliveryOrderDTO> deliveryOrderList);
}
