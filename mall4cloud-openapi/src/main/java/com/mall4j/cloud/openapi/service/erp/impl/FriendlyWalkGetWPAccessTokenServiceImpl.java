package com.mall4j.cloud.openapi.service.erp.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.StringUtils;
import com.alibaba.nacos.common.utils.UuidUtils;
import com.mall4j.cloud.api.biz.feign.WxMpApiFeignClient;
import com.mall4j.cloud.api.openapi.skq_erp.dto.FriendlyWalkCheckUserExistRequest;
import com.mall4j.cloud.api.openapi.skq_erp.dto.FriendlyWalkGetSmsCodeRequest;
import com.mall4j.cloud.api.openapi.skq_erp.dto.FriendlyWalkGetWPAccessTokenRequest;
import com.mall4j.cloud.api.openapi.skq_erp.dto.StdCommonReq;
import com.mall4j.cloud.api.openapi.skq_erp.response.FriendlyWalkCheckUserExistResponse;
import com.mall4j.cloud.api.openapi.skq_erp.response.StdResult;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.openapi.service.erp.IStdHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FriendlyWalkGetWPAccessTokenServiceImpl implements IStdHandlerService, InitializingBean {

    private static final String method = "std.universal.getWPAccessToken";
    @Autowired
    WxMpApiFeignClient wxMpApiFeignClient;


    @Override
    public StdResult stdHandler(StdCommonReq commonReq, String bodyStr) {
        long start = System.currentTimeMillis();
        String requestId = UuidUtils.generateUuid();
        StdResult stdResult = StdResult.success();
        ServerResponseEntity<String> responseEntity = null;
        try {
            if (StringUtils.isBlank(bodyStr)) {
                stdResult = StdResult.fail("请求参数为空");
                return stdResult;
            }

            FriendlyWalkGetWPAccessTokenRequest friendlyWalkGetSmsCodeRequest = JSONObject.parseObject(bodyStr,FriendlyWalkGetWPAccessTokenRequest.class);
            if(friendlyWalkGetSmsCodeRequest==null || StringUtils.isBlank(friendlyWalkGetSmsCodeRequest.getAppid())){
                stdResult = StdResult.fail("appid为空");
                return stdResult;
            }


            responseEntity = wxMpApiFeignClient.getWxMpAccessToken(friendlyWalkGetSmsCodeRequest.getAppid());
            if(responseEntity==null || responseEntity.isFail() || StringUtils.isBlank(responseEntity.getData())){
                stdResult = StdResult.fail("appid不存在或者未在小程序授权。");
                return stdResult;
            }

            stdResult.setData(responseEntity.getData());
        } catch (Exception e) {
            log.error(requestId + "-FriendlyWalk获取公众号AccessToken异常", e);
            stdResult = StdResult.fail("查询异常，请稍后再试！");
        } finally {
            log.info("FriendlyWalk获取公众号AccessToken-{}-query请求参数:{},json请求参数:{},feign调用响应为：{},请求响应:{},共耗时:{}", requestId, commonReq, bodyStr, responseEntity, stdResult, System.currentTimeMillis() - start);
        }
        return stdResult;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        register(method, this);
    }
}
