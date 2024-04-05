package com.mall4j.cloud.openapi.service.erp.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.StringUtils;
import com.alibaba.nacos.common.utils.UuidUtils;
import com.mall4j.cloud.api.docking.skq_crm.dto.TmallSmsCodeSendDto;
import com.mall4j.cloud.api.docking.skq_crm.feign.CrmCustomerFeignClient;
import com.mall4j.cloud.api.openapi.skq_erp.dto.FriendlyWalkGetSmsCodeRequest;
import com.mall4j.cloud.api.openapi.skq_erp.dto.StdCommonReq;
import com.mall4j.cloud.api.openapi.skq_erp.dto.StdShipDto;
import com.mall4j.cloud.api.openapi.skq_erp.response.StdResult;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.openapi.service.erp.IStdHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FriendlyWalkGetSmsCodeServiceImpl implements IStdHandlerService, InitializingBean {

    private static final String method = "std.universal.getSmsCode";
    @Autowired
    CrmCustomerFeignClient crmCustomerFeignClient;

    @Override
    public StdResult stdHandler(StdCommonReq commonReq, String bodyStr) {

        long start = System.currentTimeMillis();
        String requestId = UuidUtils.generateUuid();
        StdResult stdResult = StdResult.success();
        ServerResponseEntity responseEntity = null;
        try {
            List<StdShipDto> requestDatas = null;
            if (StringUtils.isBlank(bodyStr) ) {
                stdResult = StdResult.fail("请求参数为空");
                return stdResult;
            }
            FriendlyWalkGetSmsCodeRequest friendlyWalkGetSmsCodeRequest = JSONObject.parseObject(bodyStr,FriendlyWalkGetSmsCodeRequest.class);
            TmallSmsCodeSendDto tmallSmsCodeSendDto = new TmallSmsCodeSendDto();
            tmallSmsCodeSendDto.setMobile(friendlyWalkGetSmsCodeRequest.getPhone());
            crmCustomerFeignClient.tmallSmsCodeSend(tmallSmsCodeSendDto);
        } catch (Exception e) {
            log.error(requestId + "-FriendlyWalk发送短信验证码异常", e);
            stdResult = StdResult.fail("处理失败");
        } finally {
            log.info("FriendlyWalk发送短信验证码-{}-query请求参数:{},json请求参数:{},feign调用响应为：{},请求响应:{},共耗时:{}", requestId, commonReq, bodyStr, responseEntity, stdResult, System.currentTimeMillis() - start);
        }
        return stdResult;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        register(method, this);
    }
}
