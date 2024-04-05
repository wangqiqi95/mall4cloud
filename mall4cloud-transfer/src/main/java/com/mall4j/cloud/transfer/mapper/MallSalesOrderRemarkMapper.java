package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.MallSalesOrderRemark;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商城_订单_官网订单信息_退货信息_退款信息备注信息
 *
 * @author FrozenWatermelon
 * @date 2022-04-07 23:11:22
 */
public interface MallSalesOrderRemarkMapper {

	/**
	 * 获取商城_订单_官网订单信息_退货信息_退款信息备注信息列表
	 * @return 商城_订单_官网订单信息_退货信息_退款信息备注信息列表
	 */
	List<MallSalesOrderRemark> list();

	/**
	 * 根据商城_订单_官网订单信息_退货信息_退款信息备注信息id获取商城_订单_官网订单信息_退货信息_退款信息备注信息
	 *
	 * @param id 商城_订单_官网订单信息_退货信息_退款信息备注信息id
	 * @return 商城_订单_官网订单信息_退货信息_退款信息备注信息
	 */
	MallSalesOrderRemark getById(@Param("id") Long id);

    MallSalesOrderRemark getByOrderId(@Param("orderId") Long orderId);

	/**
	 * 保存商城_订单_官网订单信息_退货信息_退款信息备注信息
	 * @param mallSalesOrderRemark 商城_订单_官网订单信息_退货信息_退款信息备注信息
	 */
	void save(@Param("mallSalesOrderRemark") MallSalesOrderRemark mallSalesOrderRemark);

	/**
	 * 更新商城_订单_官网订单信息_退货信息_退款信息备注信息
	 * @param mallSalesOrderRemark 商城_订单_官网订单信息_退货信息_退款信息备注信息
	 */
	void update(@Param("mallSalesOrderRemark") MallSalesOrderRemark mallSalesOrderRemark);

	/**
	 * 根据商城_订单_官网订单信息_退货信息_退款信息备注信息id删除商城_订单_官网订单信息_退货信息_退款信息备注信息
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
