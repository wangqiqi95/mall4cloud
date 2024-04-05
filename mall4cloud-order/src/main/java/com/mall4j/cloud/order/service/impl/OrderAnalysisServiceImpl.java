package com.mall4j.cloud.order.service.impl;

import com.mall4j.cloud.api.order.dto.CustomerRetainedDTO;
import com.mall4j.cloud.api.order.vo.CustomerRetainVO;
import com.mall4j.cloud.common.cache.constant.CacheNames;
import com.mall4j.cloud.common.cache.constant.OrderCacheNames;
import com.mall4j.cloud.order.mapper.OrderAnalysisMapper;
import com.mall4j.cloud.order.service.OrderAnalysisService;
import ma.glasnost.orika.MapperFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * 订单数据分析
 * @author cl
 * @date 2021-05-25 09:13:38
 */
@Service
public class OrderAnalysisServiceImpl implements OrderAnalysisService {

    private static final Logger log = LoggerFactory.getLogger(OrderAnalysisServiceImpl.class);

    @Autowired
    private OrderAnalysisMapper orderAnalysisMapper;
    @Autowired
    private MapperFacade mapperFacade;


    @Override
    @Cacheable(cacheNames = OrderCacheNames.ORDER_TRADE_RETAINED_KEY, key = "#customerRetainedDTO.retainType +':'+ #customerRetainedDTO.dateType")
    public List<CustomerRetainVO> getTradeRetained(CustomerRetainedDTO customerRetainedDTO) {
        List<CustomerRetainVO> list = orderAnalysisMapper.getTradeRetained(customerRetainedDTO);
        return list;
    }


    private Double getDouble(Double value) {
        if (Objects.isNull(value)) {
            return 0.0;
        }
        return value;
    }
    
    @Override
    @CacheEvict(cacheNames = OrderCacheNames.ORDER_TRADE_RETAINED_KEY, key = "#customerRetainedDTO.retainType +':'+ #customerRetainedDTO.dateType")
    public void removeCacheTradeRetained(CustomerRetainedDTO customerRetainedDTO) {
    }

}
