package com.mall4j.cloud.order.service.impl;

import com.mall4j.cloud.order.mapper.OrderMapper;
import com.mall4j.cloud.order.service.MyOrderService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author FrozenWatermelon
 */
@Service
public class MyOrderServiceImpl implements MyOrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private MapperFacade mapperFacade;

}
