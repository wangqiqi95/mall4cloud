package com.mall4j.cloud.delivery.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.delivery.model.DeliveryOrderItem;
import com.mall4j.cloud.delivery.vo.DeliveryOrderItemVO;

/**
 * 物流订单项信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:00
 */
public interface DeliveryOrderItemService {

	/**
	 * 分页获取物流订单项信息列表
	 * @param pageDTO 分页参数
	 * @return 物流订单项信息列表分页数据
	 */
	PageVO<DeliveryOrderItemVO> page(PageDTO pageDTO);

	/**
	 * 根据物流订单项信息id获取物流订单项信息
	 *
	 * @param id 物流订单项信息id
	 * @return 物流订单项信息
	 */
	DeliveryOrderItemVO getById(Long id);

	/**
	 * 保存物流订单项信息
	 * @param deliveryOrderItem 物流订单项信息
	 */
	void save(DeliveryOrderItem deliveryOrderItem);

	/**
	 * 更新物流订单项信息
	 * @param deliveryOrderItem 物流订单项信息
	 */
	void update(DeliveryOrderItem deliveryOrderItem);

	/**
	 * 根据物流订单项信息id删除物流订单项信息
	 * @param id
	 */
	void deleteById(Long id);
}
