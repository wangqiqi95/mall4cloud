package com.mall4j.cloud.payment.feign;

import com.mall4j.cloud.api.payment.bo.AftersaleMerchantHandleRefundOverdueRequest;
import com.mall4j.cloud.api.payment.bo.PayRefundFeignBO;
import cn.hutool.core.bean.BeanUtil;
import com.mall4j.cloud.common.order.bo.PayRefundBO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.payment.service.RefundInfoService;
import com.mall4j.cloud.api.payment.feign.RefundFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * 退款openfeign实现类
 *
 * @luzhengxiang
 * @create 2022-05-16 2:09 AM
 **/
@RestController
public class RefundFeignClientController implements RefundFeignClient {

    @Autowired
    private RefundInfoService refundInfoService;

    @Override
    public ServerResponseEntity<Void> payRefund(PayRefundFeignBO payRefundFeignBO) {

        PayRefundBO payRefundBO = new PayRefundBO();
        BeanUtil.copyProperties(payRefundFeignBO,payRefundBO);
        refundInfoService.doRefund(payRefundBO);

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> liveStoreAftersaleMerchantHandleRefundOverdue(AftersaleMerchantHandleRefundOverdueRequest refundOverdueRequest) {
        refundInfoService.liveStoreAftersaleMerchantHandleRefundOverdue(refundOverdueRequest);
        return ServerResponseEntity.success();
    }
}
