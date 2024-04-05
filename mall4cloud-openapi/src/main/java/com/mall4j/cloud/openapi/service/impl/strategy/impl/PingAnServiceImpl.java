package com.mall4j.cloud.openapi.service.impl.strategy.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.dto.ma.WxMaGenerateSchemeRequest;
import com.mall4j.cloud.api.biz.feign.WxMaApiFeignClient;
import com.mall4j.cloud.api.openapi.dto.req.PingAnSchemeReqDto;
import com.mall4j.cloud.api.platform.feign.ConfigFeignClient;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.openapi.service.impl.strategy.Strategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service("pingAnGenerateScheme")
public class PingAnServiceImpl implements Strategy {
    @Autowired
    private WxMaApiFeignClient wxMaApiFeignClient;
    @Autowired
    private ConfigFeignClient configFeignClient;


    @Override
    public ServerResponseEntity doOperation(String data) {
        PingAnSchemeReqDto pingAnSchemeReqDto = JSON.parseObject(data, PingAnSchemeReqDto.class);

        ServerResponseEntity<String> serverResponse = configFeignClient.getConfig("PING_AN_SCHEME_URL");
        if(serverResponse.isFail() || StringUtils.isBlank(serverResponse.getData())){
            throw new LuckException("未获取到配置");
        }
        JSONObject jsonObject = JSON.parseObject(serverResponse.getData());
        String path = (String)jsonObject.get(pingAnSchemeReqDto.getType());
        if(StringUtils.isBlank(pingAnSchemeReqDto.getQuery())){
            String defaultQuery = (String)jsonObject.get("defaultQuery");
            pingAnSchemeReqDto.setQuery(defaultQuery);
        }

        WxMaGenerateSchemeRequest.JumpWxa jumpWxa = WxMaGenerateSchemeRequest.JumpWxa.newBuilder()
                .path(path)
                .query(pingAnSchemeReqDto.getQuery())
                .envVersion("release")
                .build();

        WxMaGenerateSchemeRequest request = WxMaGenerateSchemeRequest.newBuilder()
                .jumpWxa(jumpWxa)
                .expireInterval(30)
                .isExpire(true)
                .expireType(1)
                .build();

        return wxMaApiFeignClient.generateScheme(request);
    }

}
