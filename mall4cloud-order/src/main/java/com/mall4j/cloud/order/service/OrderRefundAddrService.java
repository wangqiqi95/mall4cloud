package com.mall4j.cloud.order.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.order.model.OrderRefundAddr;

/**
 * 用户退货物流地址
 *
 * @author FrozenWatermelon
 * @date 2021-03-09 13:44:31
 */
public interface OrderRefundAddrService {

	/**
	 * 分页获取用户退货物流地址列表
	 * @param pageDTO 分页参数
	 * @return 用户退货物流地址列表分页数据
	 */
	PageVO<OrderRefundAddr> page(PageDTO pageDTO);

	/**
	 * 根据用户退货物流地址id获取用户退货物流地址
	 *
	 * @param refundAddrId 用户退货物流地址id
	 * @return 用户退货物流地址
	 */
	OrderRefundAddr getByRefundAddrId(Long refundAddrId);

	/**
	 * 保存用户退货物流地址
	 * @param orderRefundAddr 用户退货物流地址
	 */
	void save(OrderRefundAddr orderRefundAddr);

	/**
	 * 更新用户退货物流地址
	 * @param orderRefundAddr 用户退货物流地址
	 */
	void update(OrderRefundAddr orderRefundAddr);

	/**
	 * 根据用户退货物流地址id删除用户退货物流地址
	 * @param refundAddrId 用户退货物流地址id
	 */
	void deleteById(Long refundAddrId);

	/**
	 * 根据退款订单id获取退款地址信息
	 * @param refundId
	 * @return
	 */
    OrderRefundAddr getByRefundId(Long refundId);
}
