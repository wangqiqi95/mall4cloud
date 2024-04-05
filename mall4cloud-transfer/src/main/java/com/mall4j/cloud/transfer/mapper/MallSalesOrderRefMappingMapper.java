package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.MallSalesOrderRefMapping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商城_订单_退款信息映射表,一个OrderDtlId会有多条退款信息,关联退款表
 *
 * @author FrozenWatermelon
 * @date 2022-04-04 22:33:31
 */
public interface MallSalesOrderRefMappingMapper {

	/**
	 * 获取商城_订单_退款信息映射表,一个OrderDtlId会有多条退款信息,关联退款表列表
	 * @return 商城_订单_退款信息映射表,一个OrderDtlId会有多条退款信息,关联退款表列表
	 */
	List<MallSalesOrderRefMapping> list();

    List<MallSalesOrderRefMapping> listByRefundId(@Param("refundId") Long refundId);

	/**
	 * 根据商城_订单_退款信息映射表,一个OrderDtlId会有多条退款信息,关联退款表id获取商城_订单_退款信息映射表,一个OrderDtlId会有多条退款信息,关联退款表
	 *
	 * @param id 商城_订单_退款信息映射表,一个OrderDtlId会有多条退款信息,关联退款表id
	 * @return 商城_订单_退款信息映射表,一个OrderDtlId会有多条退款信息,关联退款表
	 */
	MallSalesOrderRefMapping getById(@Param("id") Long id);

	/**
	 * 保存商城_订单_退款信息映射表,一个OrderDtlId会有多条退款信息,关联退款表
	 * @param mallSalesOrderRefMapping 商城_订单_退款信息映射表,一个OrderDtlId会有多条退款信息,关联退款表
	 */
	void save(@Param("mallSalesOrderRefMapping") MallSalesOrderRefMapping mallSalesOrderRefMapping);

	/**
	 * 更新商城_订单_退款信息映射表,一个OrderDtlId会有多条退款信息,关联退款表
	 * @param mallSalesOrderRefMapping 商城_订单_退款信息映射表,一个OrderDtlId会有多条退款信息,关联退款表
	 */
	void update(@Param("mallSalesOrderRefMapping") MallSalesOrderRefMapping mallSalesOrderRefMapping);

	/**
	 * 根据商城_订单_退款信息映射表,一个OrderDtlId会有多条退款信息,关联退款表id删除商城_订单_退款信息映射表,一个OrderDtlId会有多条退款信息,关联退款表
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
