package com.mall4j.cloud.openapi.service.erp.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.StringUtils;
import com.alibaba.nacos.common.utils.UuidUtils;
import com.mall4j.cloud.api.docking.skq_crm.dto.TmallSmsCodeCheckDto;
import com.mall4j.cloud.api.docking.skq_crm.feign.CrmCustomerFeignClient;
import com.mall4j.cloud.api.openapi.skq_erp.dto.FriendlyWalkCheckUserExistRequest;
import com.mall4j.cloud.api.openapi.skq_erp.dto.StdCommonReq;
import com.mall4j.cloud.api.openapi.skq_erp.dto.StdShipDto;
import com.mall4j.cloud.api.openapi.skq_erp.response.FriendlyWalkCheckUserExistResponse;
import com.mall4j.cloud.api.openapi.skq_erp.response.StdResult;
import com.mall4j.cloud.api.user.dto.FriendlyWalkRegisterRequest;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.feign.UserRegisterFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.openapi.service.erp.IStdHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FriendlyWalkCheckUserExistServiceImpl implements IStdHandlerService, InitializingBean {

    private static final String method = "std.universal.checkUserExist";

    @Autowired
    UserFeignClient userFeignClient;

    @Override
    public StdResult stdHandler(StdCommonReq commonReq, String bodyStr) {
        long start = System.currentTimeMillis();
        String requestId = UuidUtils.generateUuid();
        StdResult stdResult = StdResult.success();
        ServerResponseEntity<UserApiVO> responseEntity = null;
        try {
            if (StringUtils.isBlank(bodyStr)) {
                stdResult = StdResult.fail("请求参数为空");
                return stdResult;
            }
            FriendlyWalkCheckUserExistRequest friendlyWalkCheckUserExistRequest = JSONObject.parseObject(bodyStr,FriendlyWalkCheckUserExistRequest.class);
            if(StringUtils.isBlank(friendlyWalkCheckUserExistRequest.getPhone()) && StringUtils.isBlank(friendlyWalkCheckUserExistRequest.getUnionId())){
                stdResult = StdResult.fail("不允许手机号和unionid都为空。");
                return stdResult;
            }
            String phone = StringUtils.isNotBlank(friendlyWalkCheckUserExistRequest.getPhone())?friendlyWalkCheckUserExistRequest.getPhone():"";
            String unionId = StringUtils.isNotBlank(friendlyWalkCheckUserExistRequest.getUnionId())?friendlyWalkCheckUserExistRequest.getUnionId():"";
            responseEntity =
                    userFeignClient.checkUserExist(phone,unionId);
            if(responseEntity==null || responseEntity.isFail()){
                stdResult = StdResult.fail("查询异常，请稍后再试！");
                return stdResult;
            }

            UserApiVO userApiVO = responseEntity.getData();
            FriendlyWalkCheckUserExistResponse friendlyWalkCheckUserExistResponse = new FriendlyWalkCheckUserExistResponse();

            if(userApiVO==null || StringUtils.isBlank(userApiVO.getVipcode())){
                friendlyWalkCheckUserExistResponse.setIsExist(false);
            }else{
                friendlyWalkCheckUserExistResponse.setIsExist(true);
                friendlyWalkCheckUserExistResponse.setVipcode(userApiVO.getVipcode());
                friendlyWalkCheckUserExistResponse.setPhone(userApiVO.getPhone());
                friendlyWalkCheckUserExistResponse.setUnionId(userApiVO.getUnionId());
            }
            stdResult.setData(friendlyWalkCheckUserExistResponse);

        } catch (Exception e) {
            log.error(requestId + "-FriendlyWalk查询会员是否存在异常", e);
            stdResult = StdResult.fail("查询异常，请稍后再试！");
        } finally {
            log.info("FriendlyWalk查询会员是否存-{}-query请求参数:{},json请求参数:{},feign调用响应为：{},请求响应:{},共耗时:{}", requestId, commonReq, bodyStr, responseEntity, stdResult, System.currentTimeMillis() - start);
        }
        return stdResult;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        register(method, this);
    }
}
