package com.mall4j.cloud.openapi.service.impl.strategy.impl;

import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.openapi.service.impl.strategy.Strategy;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author axin
 * @Date 2023-05-09
 **/
@Service
public class StrategyOne implements Strategy {

    @Override
    public ServerResponseEntity doOperation(String Data) {
        return ServerResponseEntity.success("one");
    }
}
