package com.mall4j.cloud.order.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.order.model.OrderSelfStation;
import com.mall4j.cloud.order.vo.OrderSelfStationVO;

/**
 * 自提订单自提点信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-05 14:13:50
 */
public interface OrderSelfStationService {

	/**
	 * 分页获取自提订单自提点信息列表
	 * @param pageDTO 分页参数
	 * @return 自提订单自提点信息列表分页数据
	 */
	PageVO<OrderSelfStationVO> page(PageDTO pageDTO);

	/**
	 * 根据自提订单自提点信息id获取自提订单自提点信息
	 *
	 * @param id 自提订单自提点信息id
	 * @return 自提订单自提点信息
	 */
	OrderSelfStationVO getById(Long id);

	/**
	 * 保存自提订单自提点信息
	 * @param orderSelfStation 自提订单自提点信息
	 */
	void save(OrderSelfStation orderSelfStation);

	/**
	 * 更新自提订单自提点信息
	 * @param orderSelfStation 自提订单自提点信息
	 */
	void update(OrderSelfStation orderSelfStation);

	/**
	 * 根据自提订单自提点信息id删除自提订单自提点信息
	 * @param id
	 */
	void deleteById(Long id);
}
