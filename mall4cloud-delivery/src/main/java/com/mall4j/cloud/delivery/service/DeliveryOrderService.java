package com.mall4j.cloud.delivery.service;

import com.mall4j.cloud.api.delivery.dto.DeliveryOrderDTO;
import com.mall4j.cloud.api.delivery.vo.DeliveryOrderFeignVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.delivery.model.DeliveryOrder;
import com.mall4j.cloud.delivery.vo.DeliveryOrderVO;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 订单快递信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:00
 */
public interface DeliveryOrderService {

	/**
	 * 分页获取订单快递信息列表
	 * @param pageDTO 分页参数
	 * @return 订单快递信息列表分页数据
	 */
	PageVO<DeliveryOrderVO> page(PageDTO pageDTO);

	/**
	 * 根据订单快递信息id获取订单快递信息
	 *
	 * @param deliveryOrderId 订单快递信息id
	 * @return 订单快递信息
	 */
	DeliveryOrderVO getByDeliveryOrderId(Long deliveryOrderId);

	/**
	 * 保存订单快递信息
	 * @param deliveryOrder 订单快递信息
	 */
	void save(DeliveryOrder deliveryOrder);

	/**
	 * 更新订单快递信息
	 * @param deliveryOrder 订单快递信息
	 */
	void update(DeliveryOrder deliveryOrder);

	/**
	 * 根据订单快递信息id删除订单快递信息
	 * @param deliveryOrderId
	 */
	void deleteById(Long deliveryOrderId);

	/**
	 * 根据订单号获取包裹信息
	 * @param orderId 订单id
	 * @throws UnsupportedEncodingException 编码异常
	 * @return 包裹信息
	 */
	List<DeliveryOrderFeignVO> getByDeliveryByOrderId(Long orderId) throws UnsupportedEncodingException;

	/**
	 * 生成物流信息及保存物流与订单关联
	 * @param deliveryOrder 订单发货信息
	 */
    void saveDeliveryInfo(DeliveryOrderDTO deliveryOrder);

	/**
	 * 查询订单物流包裹信息
	 * @param orderId
	 * @return
	 */
	List<DeliveryOrderVO> listDetailDelivery(Long orderId);

	/**
	 * 修改订单物流包裹信息
	 * @param list
	 */
    void updateOrderDeliveries(List<DeliveryOrderDTO> list);

}
