package com.mall4j.cloud.delivery.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.delivery.model.DeliveryOrderItem;
import com.mall4j.cloud.delivery.mapper.DeliveryOrderItemMapper;
import com.mall4j.cloud.delivery.service.DeliveryOrderItemService;
import com.mall4j.cloud.delivery.vo.DeliveryOrderItemVO;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 物流订单项信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:00
 */
@Service
public class DeliveryOrderItemServiceImpl implements DeliveryOrderItemService {

    @Autowired
    private DeliveryOrderItemMapper deliveryOrderItemMapper;

    @Override
    public PageVO<DeliveryOrderItemVO> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> deliveryOrderItemMapper.list());
    }

    @Override
    public DeliveryOrderItemVO getById(Long id) {
        return deliveryOrderItemMapper.getById(id);
    }

    @Override
    public void save(DeliveryOrderItem deliveryOrderItem) {
        deliveryOrderItemMapper.save(deliveryOrderItem);
    }

    @Override
    public void update(DeliveryOrderItem deliveryOrderItem) {
        deliveryOrderItemMapper.update(deliveryOrderItem);
    }

    @Override
    public void deleteById(Long id) {
        deliveryOrderItemMapper.deleteById(id);
    }
}
