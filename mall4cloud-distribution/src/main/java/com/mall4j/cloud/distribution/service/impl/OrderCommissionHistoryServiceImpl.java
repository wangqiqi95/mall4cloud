package com.mall4j.cloud.distribution.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.dto.OrderCommissionHistoryDTO;
import com.mall4j.cloud.distribution.model.OrderCommissionHistory;
import com.mall4j.cloud.distribution.mapper.OrderCommissionHistoryMapper;
import com.mall4j.cloud.distribution.service.OrderCommissionHistoryService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 历史提现订单
 *
 * @author ZengFanChang
 * @date 2022-04-26
 */
@Service
public class OrderCommissionHistoryServiceImpl implements OrderCommissionHistoryService {

    @Autowired
    private OrderCommissionHistoryMapper orderCommissionHistoryMapper;

    @Override
    public PageVO<OrderCommissionHistory> page(PageDTO pageDTO, OrderCommissionHistoryDTO orderCommissionHistoryDTO) {
        return PageUtil.doPage(pageDTO, () -> orderCommissionHistoryMapper.list(orderCommissionHistoryDTO));
    }

    @Override
    public List<OrderCommissionHistory> listByUserAndStatus(OrderCommissionHistoryDTO orderCommissionHistoryDTO) {
        return orderCommissionHistoryMapper.list(orderCommissionHistoryDTO);
    }

    @Override
    public OrderCommissionHistory getById(Long id) {
        return orderCommissionHistoryMapper.getById(id);
    }

    @Override
    public void save(OrderCommissionHistory orderCommissionHistory) {
        orderCommissionHistoryMapper.save(orderCommissionHistory);
    }

    @Override
    public void update(OrderCommissionHistory orderCommissionHistory) {
        orderCommissionHistoryMapper.update(orderCommissionHistory);
    }

    @Override
    public void deleteById(Long id) {
        orderCommissionHistoryMapper.deleteById(id);
    }
}
