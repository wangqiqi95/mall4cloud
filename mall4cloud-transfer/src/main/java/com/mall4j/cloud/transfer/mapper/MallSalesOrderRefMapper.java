package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.MallSalesOrderRef;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商城_订单_官网订单信息_退款信息
 *
 * @author FrozenWatermelon
 * @date 2022-04-08 15:14:00
 */
public interface MallSalesOrderRefMapper {

	/**
	 * 获取商城_订单_官网订单信息_退款信息列表
	 * @return 商城_订单_官网订单信息_退款信息列表
	 */
	List<MallSalesOrderRef> list();

	/**
	 * 根据商城_订单_官网订单信息_退款信息id获取商城_订单_官网订单信息_退款信息
	 *
	 * @param id 商城_订单_官网订单信息_退款信息id
	 * @return 商城_订单_官网订单信息_退款信息
	 */
	MallSalesOrderRef getById(@Param("id") Long id);

	/**
	 * 保存商城_订单_官网订单信息_退款信息
	 * @param mallSalesOrderRef 商城_订单_官网订单信息_退款信息
	 */
	void save(@Param("mallSalesOrderRef") MallSalesOrderRef mallSalesOrderRef);

	/**
	 * 更新商城_订单_官网订单信息_退款信息
	 * @param mallSalesOrderRef 商城_订单_官网订单信息_退款信息
	 */
	void update(@Param("mallSalesOrderRef") MallSalesOrderRef mallSalesOrderRef);

	/**
	 * 根据商城_订单_官网订单信息_退款信息id删除商城_订单_官网订单信息_退款信息
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
