package com.mall4j.cloud.openapi.service.erp.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.StringUtils;
import com.alibaba.nacos.common.utils.UuidUtils;
import com.mall4j.cloud.api.coupon.feign.CouponFeignClient;
import com.mall4j.cloud.api.openapi.skq_erp.dto.FriendlyWalkCheckUserExistRequest;
import com.mall4j.cloud.api.openapi.skq_erp.dto.FriendlyWalkUserReceiveCouponRequest;
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
public class FriendlyWalkUserReceiveCouponServiceImpl implements IStdHandlerService, InitializingBean {

    private static final String method = "std.universal.userReceiveCoupon";

    @Autowired
    UserFeignClient userFeignClient;
    @Autowired
    CouponFeignClient couponFeignClient;

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
            FriendlyWalkUserReceiveCouponRequest req = JSONObject.parseObject(bodyStr, FriendlyWalkUserReceiveCouponRequest.class);
            ServerResponseEntity<UserApiVO> user = userFeignClient.getUserByUnionId(req.getUnionId());
            if(user == null || user.isFail()){
                stdResult = StdResult.fail("查询异常，请稍后再试！");
                return stdResult;
            }
            UserApiVO userApiVO = user.getData();

            ServerResponseEntity<Void> voidServerResponseEntity = couponFeignClient.userReceiveCoupon(req.getActivityId(), req.getCouponId(), req.getStoreId(), userApiVO.getUserId());
            if(voidServerResponseEntity.isFail()){
                stdResult = StdResult.fail("调用领券异常，请稍后再试");
            }

        } catch (Exception e) {
            log.error(requestId + "-FriendlyWalk发放优惠券是否存在异常", e);
            stdResult = StdResult.fail("查询异常，请稍后再试！");
        } finally {
            log.info("FriendlyWalk发放优惠券-{}-query请求参数:{},json请求参数:{},feign调用响应为：{},请求响应:{},共耗时:{}", requestId, commonReq, bodyStr, responseEntity, stdResult, System.currentTimeMillis() - start);
        }
        return stdResult;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        register(method, this);
    }
}
