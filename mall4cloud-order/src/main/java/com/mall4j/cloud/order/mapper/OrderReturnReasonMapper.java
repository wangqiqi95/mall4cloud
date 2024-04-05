package com.mall4j.cloud.order.mapper;

import com.mall4j.cloud.order.model.OrderReturnReason;
import com.mall4j.cloud.order.vo.OrderReturnReasonVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 退款原因
 *
 * @author FrozenWatermelon
 * @date 2020-12-05 14:13:50
 */
public interface OrderReturnReasonMapper {

	/**
	 * 获取退款原因列表
	 * @return 退款原因列表
	 */
	List<OrderReturnReasonVO> list();

	/**
	 * 根据退款原因id获取退款原因
	 *
	 * @param reasonId 退款原因id
	 * @return 退款原因
	 */
	OrderReturnReasonVO getByReasonId(@Param("reasonId") Long reasonId);

	/**
	 * 保存退款原因
	 * @param orderReturnReason 退款原因
	 */
	void save(@Param("orderReturnReason") OrderReturnReason orderReturnReason);

	/**
	 * 更新退款原因
	 * @param orderReturnReason 退款原因
	 */
	void update(@Param("orderReturnReason") OrderReturnReason orderReturnReason);

	/**
	 * 根据退款原因id删除退款原因
	 * @param reasonId
	 */
	void deleteById(@Param("reasonId") Long reasonId);
}
