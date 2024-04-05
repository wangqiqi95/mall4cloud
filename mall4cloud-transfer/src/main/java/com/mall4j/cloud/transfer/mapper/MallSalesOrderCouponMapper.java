package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.MallSalesOrderCoupon;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单销售电子券记录
 *
 * @author FrozenWatermelon
 * @date 2022-04-12 15:28:02
 */
public interface MallSalesOrderCouponMapper {

	/**
	 * 获取订单销售电子券记录列表
	 * @return 订单销售电子券记录列表
	 */
	List<MallSalesOrderCoupon> list();

	/**
	 * 根据订单销售电子券记录id获取订单销售电子券记录
	 *
	 * @param id 订单销售电子券记录id
	 * @return 订单销售电子券记录
	 */
	MallSalesOrderCoupon getById(@Param("id") Long id);

    MallSalesOrderCoupon getByCouponId(@Param("couponNo") String couponNo,@Param("userId")Long userId);


	/**
	 * 保存订单销售电子券记录
	 * @param mallSalesOrderCoupon 订单销售电子券记录
	 */
	void save(@Param("mallSalesOrderCoupon") MallSalesOrderCoupon mallSalesOrderCoupon);

	/**
	 * 更新订单销售电子券记录
	 * @param mallSalesOrderCoupon 订单销售电子券记录
	 */
	void update(@Param("mallSalesOrderCoupon") MallSalesOrderCoupon mallSalesOrderCoupon);

	/**
	 * 根据订单销售电子券记录id删除订单销售电子券记录
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
