package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.dto.OrderCommissionHistoryDTO;
import com.mall4j.cloud.distribution.model.OrderCommissionHistory;

import java.util.List;

/**
 * 历史提现订单
 *
 * @author ZengFanChang
 * @date 2022-04-26
 */
public interface OrderCommissionHistoryService {

	/**
	 * 分页获取历史提现订单列表
	 * @param pageDTO 分页参数
	 * @return 历史提现订单列表分页数据
	 */
	PageVO<OrderCommissionHistory> page(PageDTO pageDTO, OrderCommissionHistoryDTO orderCommissionHistoryDTO);

	List<OrderCommissionHistory> listByUserAndStatus(OrderCommissionHistoryDTO orderCommissionHistoryDTO);

	/**
	 * 根据历史提现订单id获取历史提现订单
	 *
	 * @param id 历史提现订单id
	 * @return 历史提现订单
	 */
	OrderCommissionHistory getById(Long id);

	/**
	 * 保存历史提现订单
	 * @param orderCommissionHistory 历史提现订单
	 */
	void save(OrderCommissionHistory orderCommissionHistory);

	/**
	 * 更新历史提现订单
	 * @param orderCommissionHistory 历史提现订单
	 */
	void update(OrderCommissionHistory orderCommissionHistory);

	/**
	 * 根据历史提现订单id删除历史提现订单
	 * @param id 历史提现订单id
	 */
	void deleteById(Long id);
}
