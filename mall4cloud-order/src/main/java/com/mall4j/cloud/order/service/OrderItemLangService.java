package com.mall4j.cloud.order.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.order.vo.OrderItemLangVO;
import com.mall4j.cloud.order.model.OrderItem;
import com.mall4j.cloud.order.model.OrderItemLang;

import java.util.List;

/**
 * 订单项-国际化
 *
 * @author YXF
 * @date 2021-05-17 15:26:54
 */
public interface OrderItemLangService {

	/**
	 * 分页获取订单项-国际化列表
	 * @param pageDTO 分页参数
	 * @return 订单项-国际化列表分页数据
	 */
	PageVO<OrderItemLang> page(PageDTO pageDTO);

	/**
	 * 根据订单项-国际化id获取订单项-国际化
	 *
	 * @param orderItemId 订单项-国际化id
	 * @return 订单项-国际化
	 */
	OrderItemLang getByOrderItemId(Long orderItemId);

	/**
	 * 保存订单项-国际化
	 * @param orderItemLang 订单项-国际化
	 */
	void save(OrderItemLang orderItemLang);

	/**
	 * 更新订单项-国际化
	 * @param orderItemLang 订单项-国际化
	 */
	void update(OrderItemLang orderItemLang);

	/**
	 * 根据订单项-国际化id删除订单项-国际化
	 * @param orderItemId 订单项-国际化id
	 */
	void deleteById(Long orderItemId);

	/**
	 * 保存订单项语言信息
	 * @param orderItems
	 */
	void saveOrderItemLang(List<OrderItem> orderItems);

	/**
	 * 根据订单项id列表获取订单项国际化信息列表
	 * @param orderItemIds
	 * @return
	 */
    List<OrderItemLangVO> listOrderItemLangByIds(List<Long> orderItemIds);
}
