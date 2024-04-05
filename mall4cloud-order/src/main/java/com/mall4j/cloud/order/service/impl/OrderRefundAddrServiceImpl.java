package com.mall4j.cloud.order.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.order.model.OrderRefundAddr;
import com.mall4j.cloud.order.mapper.OrderRefundAddrMapper;
import com.mall4j.cloud.order.service.OrderRefundAddrService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户退货物流地址
 *
 * @author FrozenWatermelon
 * @date 2021-03-09 13:44:31
 */
@Service
public class OrderRefundAddrServiceImpl implements OrderRefundAddrService {

    @Autowired
    private OrderRefundAddrMapper orderRefundAddrMapper;

    @Override
    public PageVO<OrderRefundAddr> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> orderRefundAddrMapper.list());
    }

    @Override
    public OrderRefundAddr getByRefundAddrId(Long refundAddrId) {
        return orderRefundAddrMapper.getByRefundAddrId(refundAddrId);
    }

    @Override
    public void save(OrderRefundAddr orderRefundAddr) {
        orderRefundAddrMapper.save(orderRefundAddr);
    }

    @Override
    public void update(OrderRefundAddr orderRefundAddr) {
        orderRefundAddrMapper.update(orderRefundAddr);
    }

    @Override
    public void deleteById(Long refundAddrId) {
        orderRefundAddrMapper.deleteById(refundAddrId);
    }

    @Override
    public OrderRefundAddr getByRefundId(Long refundId) {
        return orderRefundAddrMapper.getByRefundId(refundId);
    }
}
