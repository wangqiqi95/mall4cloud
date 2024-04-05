package com.mall4j.cloud.order.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.order.model.OrderReturnReason;
import com.mall4j.cloud.order.vo.OrderReturnReasonVO;

/**
 * 退款原因
 *
 * @author FrozenWatermelon
 * @date 2020-12-05 14:13:50
 */
public interface OrderReturnReasonService {

	/**
	 * 分页获取退款原因列表
	 * @param pageDTO 分页参数
	 * @return 退款原因列表分页数据
	 */
	PageVO<OrderReturnReasonVO> page(PageDTO pageDTO);

	/**
	 * 根据退款原因id获取退款原因
	 *
	 * @param reasonId 退款原因id
	 * @return 退款原因
	 */
	OrderReturnReasonVO getByReasonId(Long reasonId);

	/**
	 * 保存退款原因
	 * @param orderReturnReason 退款原因
	 */
	void save(OrderReturnReason orderReturnReason);

	/**
	 * 更新退款原因
	 * @param orderReturnReason 退款原因
	 */
	void update(OrderReturnReason orderReturnReason);

	/**
	 * 根据退款原因id删除退款原因
	 * @param reasonId
	 */
	void deleteById(Long reasonId);
}
