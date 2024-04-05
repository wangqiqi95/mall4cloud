package com.mall4j.cloud.api.payment.feign;

import com.mall4j.cloud.api.payment.bo.AftersaleMerchantHandleRefundOverdueRequest;
import com.mall4j.cloud.api.payment.bo.PayRefundFeignBO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 退款feign处理类
 *
 * @luzhengxiang
 * @create 2022-05-16 1:59 AM
 **/
@FeignClient(value = "mall4cloud-payment",contextId = "refund")
public interface RefundFeignClient {

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/payRefund")
    ServerResponseEntity<Void> payRefund(@RequestBody PayRefundFeignBO payRefundBO);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/AftersaleMerchantHandleRefundOverdue")
    ServerResponseEntity<Void> liveStoreAftersaleMerchantHandleRefundOverdue(@RequestBody AftersaleMerchantHandleRefundOverdueRequest refundOverdueRequest);

}
