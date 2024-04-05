package com.mall4j.cloud.order.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.mall4j.cloud.api.order.vo.OrderSettlementSimpleVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.order.model.OrderSettlement;
import com.mall4j.cloud.order.mapper.OrderSettlementMapper;
import com.mall4j.cloud.order.service.OrderSettlementService;
import com.mall4j.cloud.order.vo.OrderSettlementVO;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单结算表
 *
 * @author FrozenWatermelon
 * @date 2020-12-05 14:13:50
 */
@Service
public class OrderSettlementServiceImpl implements OrderSettlementService {

    @Autowired
    private OrderSettlementMapper orderSettlementMapper;

    @Override
    public PageVO<OrderSettlementVO> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> orderSettlementMapper.list());
    }

    @Override
    public OrderSettlementVO getBySettlementId(Long settlementId) {
        return orderSettlementMapper.getBySettlementId(settlementId);
    }

    @Override
    public void update(OrderSettlement orderSettlement) {
        orderSettlementMapper.update(orderSettlement);
    }

    @Override
    public void deleteById(Long settlementId) {
        orderSettlementMapper.deleteById(settlementId);
    }

    @Override
    public OrderSettlement getByOrderId(Long orderId) {
        return orderSettlementMapper.getByOrderId(orderId);
    }

    @Override
    public List<OrderSettlementSimpleVO> listOrderIdsByPayIds(List<Long> payIds) {
        if (CollUtil.isEmpty(payIds)) {
            return new ArrayList<>();
        }
        return orderSettlementMapper.listOrderIdsByPayIds(payIds);
    }
}
