package com.mall4j.cloud.openapi.service.impl.strategy;

import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.api.openapi.dto.AppConfig;
import com.mall4j.cloud.api.openapi.dto.req.TripartiteCommonReq;
import com.mall4j.cloud.api.openapi.utils.TripartiteCommonUtil;
import com.mall4j.cloud.api.platform.feign.ConfigFeignClient;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author axin
 * @Date 2023-05-09
 **/
@Component
@Slf4j
public class FactoryForStrategy {
    @Autowired
    Map<String, Strategy> strategyMap = new ConcurrentHashMap<>(8);

    @Autowired
    private ConfigFeignClient configFeignClient;

    /**
     * 验签处理
     * @param commonReq
     * @param data
     * @return
     */
    public Strategy tripartiteHandler(TripartiteCommonReq commonReq, String data){
        ServerResponseEntity<String> tripartiteService = configFeignClient.getConfig("TRIPARTITE_SERVICE");
        if(tripartiteService.isFail() || StringUtils.isBlank(tripartiteService.getData())){
            throw new LuckException("获取配置失败");
        }

        List<AppConfig> appConfigs = JSON.parseArray(tripartiteService.getData(), AppConfig.class);
        Map<String, String> appConfigMap = appConfigs.stream().collect(Collectors.toMap(AppConfig::getAppKey, AppConfig::getAppSecret));
        if(!appConfigMap.containsKey(commonReq.getAppKey())){
            log.error("appKey不匹配");
            throw new LuckException("验签失败!");
        }

        boolean check=TripartiteCommonUtil.verifySign(commonReq,commonReq.getAppKey(),appConfigMap.get(commonReq.getAppKey()),data);
        if(!check){
            throw new LuckException("验签失败!");
        }

        return getStrategy(commonReq.getMethod());
    }


    private Strategy getStrategy(String component){
        Strategy strategy = strategyMap.get(component);
        if(strategy == null) {
            throw new LuckException("no strategy defined");
        }
        return strategy;
    }
}
