package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.OrderRefund;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单退款记录信息
 *
 * @author FrozenWatermelon
 * @date 2022-04-04 23:32:09
 */
public interface OrderRefundMapper {

	/**
	 * 获取订单退款记录信息列表
	 * @return 订单退款记录信息列表
	 */
	List<OrderRefund> list();

	/**
	 * 根据订单退款记录信息id获取订单退款记录信息
	 *
	 * @param refundId 订单退款记录信息id
	 * @return 订单退款记录信息
	 */
	OrderRefund getByRefundId(@Param("refundId") Long refundId);

	/**
	 * 保存订单退款记录信息
	 * @param orderRefund 订单退款记录信息
	 */
	void save(@Param("orderRefund") OrderRefund orderRefund);
    void save2(@Param("orderRefund") OrderRefund orderRefund);

    void batchSave(@Param("orderRefunds") List<OrderRefund> orderRefunds);

	/**
	 * 更新订单退款记录信息
	 * @param orderRefund 订单退款记录信息
	 */
	void update(@Param("orderRefund") OrderRefund orderRefund);

	/**
	 * 根据订单退款记录信息id删除订单退款记录信息
	 * @param refundId
	 */
	void deleteById(@Param("refundId") Long refundId);
}
