package com.mall4j.cloud.order.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.order.model.OrderRefundSettlement;
import com.mall4j.cloud.order.mapper.OrderRefundSettlementMapper;
import com.mall4j.cloud.order.service.OrderRefundSettlementService;
import com.mall4j.cloud.order.vo.OrderRefundSettlementVO;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 退款支付结算单据
 *
 * @author FrozenWatermelon
 * @date 2020-12-05 14:13:50
 */
@Service
public class OrderRefundSettlementServiceImpl implements OrderRefundSettlementService {

    @Autowired
    private OrderRefundSettlementMapper orderRefundSettlementMapper;

    @Override
    public PageVO<OrderRefundSettlementVO> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> orderRefundSettlementMapper.list());
    }

    @Override
    public OrderRefundSettlementVO getBySettlementId(Long settlementId) {
        return orderRefundSettlementMapper.getBySettlementId(settlementId);
    }

    @Override
    public void save(OrderRefundSettlement orderRefundSettlement) {
        orderRefundSettlementMapper.save(orderRefundSettlement);
    }

    @Override
    public void update(OrderRefundSettlement orderRefundSettlement) {
        orderRefundSettlementMapper.update(orderRefundSettlement);
    }

    @Override
    public void deleteById(Long settlementId) {
        orderRefundSettlementMapper.deleteById(settlementId);
    }
}
