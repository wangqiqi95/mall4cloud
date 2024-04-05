package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.MallSalesOrderRt;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商城_订单_官网订单信息_退货信息
 *
 * @author FrozenWatermelon
 * @date 2022-04-10 22:09:01
 */
public interface MallSalesOrderRtMapper {

	/**
	 * 获取商城_订单_官网订单信息_退货信息列表
	 * @return 商城_订单_官网订单信息_退货信息列表
	 */
	List<MallSalesOrderRt> list();

    List<MallSalesOrderRt> list2();

	/**
	 * 根据商城_订单_官网订单信息_退货信息id获取商城_订单_官网订单信息_退货信息
	 *
	 * @param id 商城_订单_官网订单信息_退货信息id
	 * @return 商城_订单_官网订单信息_退货信息
	 */
	MallSalesOrderRt getById(@Param("id") Long id);

	/**
	 * 保存商城_订单_官网订单信息_退货信息
	 * @param mallSalesOrderRt 商城_订单_官网订单信息_退货信息
	 */
	void save(@Param("mallSalesOrderRt") MallSalesOrderRt mallSalesOrderRt);

	/**
	 * 更新商城_订单_官网订单信息_退货信息
	 * @param mallSalesOrderRt 商城_订单_官网订单信息_退货信息
	 */
	void update(@Param("mallSalesOrderRt") MallSalesOrderRt mallSalesOrderRt);

	/**
	 * 根据商城_订单_官网订单信息_退货信息id删除商城_订单_官网订单信息_退货信息
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
