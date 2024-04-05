package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.OrderRefundAddr;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户退货物流地址
 *
 * @author FrozenWatermelon
 * @date 2022-04-04 23:32:09
 */
public interface OrderRefundAddrMapper {

	/**
	 * 获取用户退货物流地址列表
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
	 * @param orderRefundAddr 用户退货物流地址
	 */
	void save(@Param("orderRefundAddr") OrderRefundAddr orderRefundAddr);

    void batchSave(@Param("orderRefundAddrs") List<OrderRefundAddr> orderRefundAddr);

	/**
	 * 更新用户退货物流地址
	 * @param orderRefundAddr 用户退货物流地址
	 */
	void update(@Param("orderRefundAddr") OrderRefundAddr orderRefundAddr);

	/**
	 * 根据用户退货物流地址id删除用户退货物流地址
	 * @param refundAddrId
	 */
	void deleteById(@Param("refundAddrId") Long refundAddrId);
}
