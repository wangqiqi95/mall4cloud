package com.mall4j.cloud.order.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.order.model.OrderRefundSettlement;
import com.mall4j.cloud.order.vo.OrderRefundSettlementVO;

/**
 * 退款支付结算单据
 *
 * @author FrozenWatermelon
 * @date 2020-12-05 14:13:50
 */
public interface OrderRefundSettlementService {

	/**
	 * 分页获取退款支付结算单据列表
	 * @param pageDTO 分页参数
	 * @return 退款支付结算单据列表分页数据
	 */
	PageVO<OrderRefundSettlementVO> page(PageDTO pageDTO);

	/**
	 * 根据退款支付结算单据id获取退款支付结算单据
	 *
	 * @param settlementId 退款支付结算单据id
	 * @return 退款支付结算单据
	 */
	OrderRefundSettlementVO getBySettlementId(Long settlementId);

	/**
	 * 保存退款支付结算单据
	 * @param orderRefundSettlement 退款支付结算单据
	 */
	void save(OrderRefundSettlement orderRefundSettlement);

	/**
	 * 更新退款支付结算单据
	 * @param orderRefundSettlement 退款支付结算单据
	 */
	void update(OrderRefundSettlement orderRefundSettlement);

	/**
	 * 根据退款支付结算单据id删除退款支付结算单据
	 * @param settlementId
	 */
	void deleteById(Long settlementId);
}
