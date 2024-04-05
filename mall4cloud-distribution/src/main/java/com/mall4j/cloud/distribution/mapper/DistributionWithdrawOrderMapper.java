package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.model.DistributionWithdrawOrder;
import com.mall4j.cloud.distribution.model.DistributionWithdrawRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 佣金提现订单信息
 *
 * @author ZengFanChang
 * @date 2021-12-11 19:35:15
 */
public interface DistributionWithdrawOrderMapper {

	/**
	 * 获取佣金提现订单信息列表
	 * @return 佣金提现订单信息列表
	 */
	List<DistributionWithdrawOrder> list();

	/**
	 * 根据佣金提现订单信息id获取佣金提现订单信息
	 *
	 * @param id 佣金提现订单信息id
	 * @return 佣金提现订单信息
	 */
	DistributionWithdrawOrder getById(@Param("id") Long id);

	/**
	 * 保存佣金提现订单信息
	 * @param distributionWithdrawOrder 佣金提现订单信息
	 */
	void save(@Param("distributionWithdrawOrder") DistributionWithdrawOrder distributionWithdrawOrder);

	/**
	 * 更新佣金提现订单信息
	 * @param distributionWithdrawOrder 佣金提现订单信息
	 */
	void update(@Param("distributionWithdrawOrder") DistributionWithdrawOrder distributionWithdrawOrder);

	/**
	 * 根据佣金提现订单信息id删除佣金提现订单信息
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

    List<DistributionWithdrawOrder> listDistributionWithdrawOrderByRecord(@Param("recordId") Long recordId);

    DistributionWithdrawRecord getDistributionWithdrawRecordByOrderId(@Param("orderId") Long orderId);
}
