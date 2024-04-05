package com.mall4j.cloud.openapi.service.impl.strategy;

import com.mall4j.cloud.common.response.ServerResponseEntity;

/**
 * @Description
 * @Author axin
 * @Date 2023-05-09
 **/
public interface Strategy {
    ServerResponseEntity doOperation(String data);
}
