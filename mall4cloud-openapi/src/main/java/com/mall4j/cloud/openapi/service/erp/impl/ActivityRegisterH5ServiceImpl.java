package com.mall4j.cloud.openapi.service.erp.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.StringUtils;
import com.alibaba.nacos.common.utils.UuidUtils;
import com.mall4j.cloud.api.docking.skq_crm.dto.TmallSmsCodeCheckDto;
import com.mall4j.cloud.api.docking.skq_crm.feign.CrmCustomerFeignClient;
import com.mall4j.cloud.api.openapi.skq_erp.dto.StdCommonReq;
import com.mall4j.cloud.api.openapi.skq_erp.response.StdResult;
import com.mall4j.cloud.api.user.constant.RegSourceEnum;
import com.mall4j.cloud.api.user.dto.FriendlyWalkRegisterRequest;
import com.mall4j.cloud.api.user.feign.UserRegisterFeignClient;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.openapi.service.erp.IStdHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ActivityRegisterH5ServiceImpl implements IStdHandlerService, InitializingBean {

    private static final String method = "std.universal.registerh5";
    public static String CHECK_REGISTER_SMS_FLAG = "checkRegisterSmsFlag";
    @Autowired
    CrmCustomerFeignClient crmCustomerFeignClient;
    @Autowired
    UserRegisterFeignClient userRegisterFeignClient;

    @Override
    public StdResult stdHandler(StdCommonReq commonReq, String bodyStr) {
        long start = System.currentTimeMillis();
        String requestId = UuidUtils.generateUuid();
        StdResult stdResult = StdResult.success();
        ServerResponseEntity responseEntity = null;
        try {
            if (StringUtils.isBlank(bodyStr)) {
                stdResult = StdResult.fail("请求参数为空");
                return stdResult;
            }
            FriendlyWalkRegisterRequest friendlyWalkRegisterRequest = JSONObject.parseObject(bodyStr,FriendlyWalkRegisterRequest.class);
            if(StringUtils.isBlank(friendlyWalkRegisterRequest.getCode()) ){
                stdResult = StdResult.fail("短信验证码参数为空");
                return stdResult;
            }
            if(StringUtils.isBlank(friendlyWalkRegisterRequest.getPhone()) ){
                stdResult = StdResult.fail("手机号码参数为空");
                return stdResult;
            }
            if(StringUtils.isBlank(friendlyWalkRegisterRequest.getUnionId()) ){
                stdResult = StdResult.fail("unionid参数为空");
                return stdResult;
            }
            //验证短信验证码是否正确
            TmallSmsCodeCheckDto tmallSmsCodeCheckDto = new TmallSmsCodeCheckDto();
            tmallSmsCodeCheckDto.setCode(friendlyWalkRegisterRequest.getCode());
            tmallSmsCodeCheckDto.setMobile(friendlyWalkRegisterRequest.getPhone());
            ServerResponseEntity serverResponseEntity = crmCustomerFeignClient.tmallSmsCodeCheck(tmallSmsCodeCheckDto);
            String checkRegisterSmsFlag = IdUtil.simpleUUID();
            if (serverResponseEntity.isSuccess()) {
                RedisUtil.set(CHECK_REGISTER_SMS_FLAG + checkRegisterSmsFlag, friendlyWalkRegisterRequest.getPhone(), 600);
            } else {
                stdResult = StdResult.fail(ResponseEnum.VERIFICATION_CODE_ERROR.getMsg());
                return stdResult;
            }

            /**
             * 注册写入
             */
            friendlyWalkRegisterRequest.setRegSource(RegSourceEnum.ETO_H5.getCode());
            ServerResponseEntity<String>  response = userRegisterFeignClient.friendlyWalkRegister(friendlyWalkRegisterRequest);
            if(response.isFail()){
                stdResult = StdResult.fail(response.getMsg());
            }else{
                stdResult.setData(response.getData());
            }
        } catch (Exception e) {
            log.error(requestId + "-活动H5注册异常", e);
            stdResult = StdResult.fail("处理失败");
        } finally {
            log.info("活动H5注册-{}-query请求参数:{},json请求参数:{},feign调用响应为：{},请求响应:{},共耗时:{}", requestId, commonReq, bodyStr, responseEntity, stdResult, System.currentTimeMillis() - start);
        }
        return stdResult;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        register(method, this);
    }
}
