package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.MallSalesOrderRtMapping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商城_订单_退货信息映射表,一个OrderDtlId会有多条退货信息,关联退货表
 *
 * @author FrozenWatermelon
 * @date 2022-04-10 22:09:01
 */
public interface MallSalesOrderRtMappingMapper {

	/**
	 * 获取商城_订单_退货信息映射表,一个OrderDtlId会有多条退货信息,关联退货表列表
	 * @return 商城_订单_退货信息映射表,一个OrderDtlId会有多条退货信息,关联退货表列表
	 */
	List<MallSalesOrderRtMapping> list();

	/**
	 * 根据商城_订单_退货信息映射表,一个OrderDtlId会有多条退货信息,关联退货表id获取商城_订单_退货信息映射表,一个OrderDtlId会有多条退货信息,关联退货表
	 *
	 * @param id 商城_订单_退货信息映射表,一个OrderDtlId会有多条退货信息,关联退货表id
	 * @return 商城_订单_退货信息映射表,一个OrderDtlId会有多条退货信息,关联退货表
	 */
	MallSalesOrderRtMapping getById(@Param("id") Long id);

    List<MallSalesOrderRtMapping> listByReturnId(@Param("returnId") Long returnId);

	/**
	 * 保存商城_订单_退货信息映射表,一个OrderDtlId会有多条退货信息,关联退货表
	 * @param mallSalesOrderRtMapping 商城_订单_退货信息映射表,一个OrderDtlId会有多条退货信息,关联退货表
	 */
	void save(@Param("mallSalesOrderRtMapping") MallSalesOrderRtMapping mallSalesOrderRtMapping);

	/**
	 * 更新商城_订单_退货信息映射表,一个OrderDtlId会有多条退货信息,关联退货表
	 * @param mallSalesOrderRtMapping 商城_订单_退货信息映射表,一个OrderDtlId会有多条退货信息,关联退货表
	 */
	void update(@Param("mallSalesOrderRtMapping") MallSalesOrderRtMapping mallSalesOrderRtMapping);

	/**
	 * 根据商城_订单_退货信息映射表,一个OrderDtlId会有多条退货信息,关联退货表id删除商城_订单_退货信息映射表,一个OrderDtlId会有多条退货信息,关联退货表
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
