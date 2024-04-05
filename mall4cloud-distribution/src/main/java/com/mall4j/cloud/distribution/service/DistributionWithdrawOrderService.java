package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.api.order.bo.EsOrderBO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.dto.DistributionWithdrawOrderDTO;
import com.mall4j.cloud.distribution.model.DistributionWithdrawOrder;

import java.util.List;

/**
 * 佣金提现订单信息
 *
 * @author ZengFanChang
 * @date 2021-12-11 19:35:15
 */
public interface DistributionWithdrawOrderService {

	/**
	 * 分页获取佣金提现订单信息列表
	 * @param pageDTO 分页参数
	 * @return 佣金提现订单信息列表分页数据
	 */
	PageVO<DistributionWithdrawOrder> page(PageDTO pageDTO);

	/**
	 * 根据佣金提现订单信息id获取佣金提现订单信息
	 *
	 * @param id 佣金提现订单信息id
	 * @return 佣金提现订单信息
	 */
	DistributionWithdrawOrder getById(Long id);

	/**
	 * 保存佣金提现订单信息
	 * @param distributionWithdrawOrder 佣金提现订单信息
	 */
	void save(DistributionWithdrawOrder distributionWithdrawOrder);

	/**
	 * 更新佣金提现订单信息
	 * @param distributionWithdrawOrder 佣金提现订单信息
	 */
	void update(DistributionWithdrawOrder distributionWithdrawOrder);

	/**
	 * 根据佣金提现订单信息id删除佣金提现订单信息
	 * @param id 佣金提现订单信息id
	 */
	void deleteById(Long id);

	List<DistributionWithdrawOrder> listDistributionWithdrawOrderByRecord(Long recordId);

	List<EsOrderBO> listWithdrawOrder(DistributionWithdrawOrderDTO dto);

}
