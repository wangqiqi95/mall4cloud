package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.dto.OrderCommissionHistoryDTO;
import com.mall4j.cloud.distribution.model.OrderCommissionHistory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 历史提现订单
 *
 * @author ZengFanChang
 * @date 2022-04-26
 */
public interface OrderCommissionHistoryMapper {

	/**
	 * 获取历史提现订单列表
	 * @return 历史提现订单列表
	 */
	List<OrderCommissionHistory> list(@Param("orderCommissionHistoryDTO") OrderCommissionHistoryDTO orderCommissionHistoryDTO);

	List<OrderCommissionHistory> listOrderInIds(@Param("ids") List<Long> ids);

	/**
	 * 根据历史提现订单id获取历史提现订单
	 *
	 * @param id 历史提现订单id
	 * @return 历史提现订单
	 */
	OrderCommissionHistory getById(@Param("id") Long id);

	/**
	 * 保存历史提现订单
	 * @param orderCommissionHistory 历史提现订单
	 */
	void save(@Param("orderCommissionHistory") OrderCommissionHistory orderCommissionHistory);

	/**
	 * 更新历史提现订单
	 * @param orderCommissionHistory 历史提现订单
	 */
	void update(@Param("orderCommissionHistory") OrderCommissionHistory orderCommissionHistory);

	/**
	 * 根据历史提现订单id删除历史提现订单
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
