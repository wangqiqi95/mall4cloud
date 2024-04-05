package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.PayInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单支付记录
 *
 * @author FrozenWatermelon
 * @date 2022-04-04 23:32:09
 */
public interface PayInfoMapper {

	/**
	 * 获取订单支付记录列表
	 * @return 订单支付记录列表
	 */
	List<PayInfo> list();

	/**
	 * 根据订单支付记录id获取订单支付记录
	 *
	 * @param payId 订单支付记录id
	 * @return 订单支付记录
	 */
	PayInfo getByPayId(@Param("payId") Long payId);

	/**
	 * 保存订单支付记录
	 * @param payInfo 订单支付记录
	 */
	void save(@Param("payInfo") PayInfo payInfo);

	/**
	 * 更新订单支付记录
	 * @param payInfo 订单支付记录
	 */
	void update(@Param("payInfo") PayInfo payInfo);

	/**
	 * 根据订单支付记录id删除订单支付记录
	 * @param payId
	 */
	void deleteById(@Param("payId") Long payId);
}
