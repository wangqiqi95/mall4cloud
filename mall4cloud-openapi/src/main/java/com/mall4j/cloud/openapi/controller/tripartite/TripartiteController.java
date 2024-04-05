package com.mall4j.cloud.openapi.controller.tripartite;

import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.api.openapi.dto.req.TripartiteCommonReq;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.openapi.service.impl.strategy.FactoryForStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Description 三方服务
 * @Author axin
 * @Date 2023-05-09 14:17
 **/
@RestController
@Slf4j
public class TripartiteController {

    @Autowired
    private FactoryForStrategy factory;

    @PostMapping("/api/external/service")
    public ServerResponseEntity<?> tripartiteService(@Valid TripartiteCommonReq commonReq, @RequestBody String data){
        log.info("三方服务调用入参:{}",data);
        long startTime=System.currentTimeMillis();
        ServerResponseEntity<?> response = factory.tripartiteHandler(commonReq, data).doOperation(data);
        log.info("三方服务调用出参:{},rt:{}", JSON.toJSONString(response),System.currentTimeMillis()-startTime);
        return response;
    }
}
