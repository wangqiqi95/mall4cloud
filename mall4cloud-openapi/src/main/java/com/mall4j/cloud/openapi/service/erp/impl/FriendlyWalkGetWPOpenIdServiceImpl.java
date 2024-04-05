package com.mall4j.cloud.openapi.service.erp.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.StringUtils;
import com.alibaba.nacos.common.utils.UuidUtils;
import com.mall4j.cloud.api.biz.feign.WxMpApiFeignClient;
import com.mall4j.cloud.api.openapi.skq_erp.dto.FriendlyWalkGetWPAccessTokenRequest;
import com.mall4j.cloud.api.openapi.skq_erp.dto.FriendlyWalkGetWPOpenIdRequest;
import com.mall4j.cloud.api.openapi.skq_erp.dto.StdCommonReq;
import com.mall4j.cloud.api.openapi.skq_erp.response.FriendlyWalkGetWPopenIdResponse;
import com.mall4j.cloud.api.openapi.skq_erp.response.StdResult;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserWeixinAccountFollowVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.openapi.service.erp.IStdHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FriendlyWalkGetWPOpenIdServiceImpl implements IStdHandlerService, InitializingBean {

    private static final String method = "std.universal.getWPOpenId";
    @Autowired
    UserFeignClient userFeignClient;


    @Override
    public StdResult stdHandler(StdCommonReq commonReq, String bodyStr) {
        long start = System.currentTimeMillis();
        String requestId = UuidUtils.generateUuid();
        StdResult stdResult = StdResult.success();
        ServerResponseEntity<UserWeixinAccountFollowVO> responseEntity = null;
        try {
            if (StringUtils.isBlank(bodyStr)) {
                stdResult = StdResult.fail("请求参数为空");
                return stdResult;
            }

            FriendlyWalkGetWPOpenIdRequest friendlyWalkGetSmsCodeRequest = JSONObject.parseObject(bodyStr,FriendlyWalkGetWPOpenIdRequest.class);
            if(friendlyWalkGetSmsCodeRequest==null || StringUtils.isBlank(friendlyWalkGetSmsCodeRequest.getAppid())){
                stdResult = StdResult.fail("公众号appid不允许为空");
                return stdResult;
            }

            if(friendlyWalkGetSmsCodeRequest==null || StringUtils.isBlank(friendlyWalkGetSmsCodeRequest.getUnionId())){
                stdResult = StdResult.fail("会员unionId不允许为空");
                return stdResult;
            }

            responseEntity =
                    userFeignClient.getUserWeixinAccountFollow(friendlyWalkGetSmsCodeRequest.getUnionId(),friendlyWalkGetSmsCodeRequest.getAppid());

            if(responseEntity==null || responseEntity.isFail() || responseEntity.getData()==null){
                stdResult = StdResult.fail("appid不存在或者当前用户未关注公众号！");
                return stdResult;
            }
            FriendlyWalkGetWPopenIdResponse getWPopenIdResponse = new FriendlyWalkGetWPopenIdResponse();
            getWPopenIdResponse.setOpenId(responseEntity.getData().getOpenId());
            getWPopenIdResponse.setStatus(responseEntity.getData().getStatus());
            stdResult.setData(getWPopenIdResponse);
        } catch (Exception e) {
            log.error(requestId + "-FriendlyWalk获取公众号AccessToken异常", e);
            stdResult = StdResult.fail("查询异常，请稍后再试！");
        } finally {
            log.info("FriendlyWalk 获取会员关注公众号的openid-{}-query请求参数:{},json请求参数:{},feign调用响应为：{},请求响应:{},共耗时:{}", requestId, commonReq, bodyStr, responseEntity, stdResult, System.currentTimeMillis() - start);
        }
        return stdResult;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        register(method, this);
    }
}
