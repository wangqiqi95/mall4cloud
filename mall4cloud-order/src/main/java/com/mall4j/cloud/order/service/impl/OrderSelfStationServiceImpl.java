package com.mall4j.cloud.order.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.order.model.OrderSelfStation;
import com.mall4j.cloud.order.mapper.OrderSelfStationMapper;
import com.mall4j.cloud.order.service.OrderSelfStationService;
import com.mall4j.cloud.order.vo.OrderSelfStationVO;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 自提订单自提点信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-05 14:13:50
 */
@Service
public class OrderSelfStationServiceImpl implements OrderSelfStationService {

    @Autowired
    private OrderSelfStationMapper orderSelfStationMapper;

    @Override
    public PageVO<OrderSelfStationVO> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> orderSelfStationMapper.list());
    }

    @Override
    public OrderSelfStationVO getById(Long id) {
        return orderSelfStationMapper.getById(id);
    }

    @Override
    public void save(OrderSelfStation orderSelfStation) {
        orderSelfStationMapper.save(orderSelfStation);
    }

    @Override
    public void update(OrderSelfStation orderSelfStation) {
        orderSelfStationMapper.update(orderSelfStation);
    }

    @Override
    public void deleteById(Long id) {
        orderSelfStationMapper.deleteById(id);
    }
}
