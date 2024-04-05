package com.mall4j.cloud.order.mapper;

import com.mall4j.cloud.order.model.OrderSelfStation;
import com.mall4j.cloud.order.vo.OrderSelfStationVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 自提订单自提点信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-05 14:13:50
 */
public interface OrderSelfStationMapper {

	/**
	 * 获取自提订单自提点信息列表
	 * @return 自提订单自提点信息列表
	 */
	List<OrderSelfStationVO> list();

	/**
	 * 根据自提订单自提点信息id获取自提订单自提点信息
	 *
	 * @param id 自提订单自提点信息id
	 * @return 自提订单自提点信息
	 */
	OrderSelfStationVO getById(@Param("id") Long id);

	/**
	 * 保存自提订单自提点信息
	 * @param orderSelfStation 自提订单自提点信息
	 */
	void save(@Param("orderSelfStation") OrderSelfStation orderSelfStation);

	/**
	 * 更新自提订单自提点信息
	 * @param orderSelfStation 自提订单自提点信息
	 */
	void update(@Param("orderSelfStation") OrderSelfStation orderSelfStation);

	/**
	 * 根据自提订单自提点信息id删除自提订单自提点信息
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
