package com.mall4j.cloud.openapi.service.eto.impl;

import com.mall4j.cloud.api.coupon.feign.CouponFeignClient;
import com.mall4j.cloud.api.openapi.skq_crm.response.CrmResponseEnum;
import com.mall4j.cloud.api.openapi.skq_erp.response.ErpResponse;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.openapi.service.eto.EtoCouponService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("etoCouponService")
public class EtoCouponServiceImpl implements EtoCouponService {

    private final Logger logger = LoggerFactory.getLogger(EtoCouponServiceImpl.class);

    @Autowired
    CouponFeignClient couponFeignClient;

    @Override
    public ErpResponse userReceiveCoupon(Long id, Long couponId, Long storeId, Long userId) {
        long start = System.currentTimeMillis();
        ErpResponse erpResponse = ErpResponse.fail();
        ServerResponseEntity responseEntity = null;
        try {
            if (erpResponse.isSuccess()) {
                responseEntity = couponFeignClient.userReceiveCoupon(id, couponId, storeId, userId);
                if (responseEntity.isSuccess()) {
                    erpResponse = ErpResponse.success();
                } else {
                    erpResponse = ErpResponse.fail(CrmResponseEnum.EXCEPTION.value(), responseEntity.getMsg());
                }
            }
            return erpResponse;
        } finally {
            logger.info("对接ETO优惠券发放-用户领券,接收到请求参数：领券活动ID{}、券ID{}、店铺ID{}，feign调用响应为：{},返回响应为：{}，共耗时：{}", id, couponId, storeId, responseEntity, erpResponse, System.currentTimeMillis() - start);
        }
    }

}
