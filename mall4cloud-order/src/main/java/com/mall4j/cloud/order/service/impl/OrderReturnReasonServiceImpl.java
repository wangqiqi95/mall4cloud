package com.mall4j.cloud.order.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.order.model.OrderReturnReason;
import com.mall4j.cloud.order.mapper.OrderReturnReasonMapper;
import com.mall4j.cloud.order.service.OrderReturnReasonService;
import com.mall4j.cloud.order.vo.OrderReturnReasonVO;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 退款原因
 *
 * @author FrozenWatermelon
 * @date 2020-12-05 14:13:50
 */
@Service
public class OrderReturnReasonServiceImpl implements OrderReturnReasonService {

    @Autowired
    private OrderReturnReasonMapper orderReturnReasonMapper;

    @Override
    public PageVO<OrderReturnReasonVO> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> orderReturnReasonMapper.list());
    }

    @Override
    public OrderReturnReasonVO getByReasonId(Long reasonId) {
        return orderReturnReasonMapper.getByReasonId(reasonId);
    }

    @Override
    public void save(OrderReturnReason orderReturnReason) {
        orderReturnReasonMapper.save(orderReturnReason);
    }

    @Override
    public void update(OrderReturnReason orderReturnReason) {
        orderReturnReasonMapper.update(orderReturnReason);
    }

    @Override
    public void deleteById(Long reasonId) {
        orderReturnReasonMapper.deleteById(reasonId);
    }
}
