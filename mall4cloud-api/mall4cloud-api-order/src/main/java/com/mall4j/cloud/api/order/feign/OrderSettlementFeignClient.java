package com.mall4j.cloud.api.order.feign;

import com.mall4j.cloud.api.order.vo.OrderSettlementSimpleVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author lth
 * @Date 2021/7/14 13:21
 */
@FeignClient(value = "mall4cloud-order",contextId = "order-settlement")
public interface OrderSettlementFeignClient {

    /**
     * 根据支付单号列表获取订单号列表
     * @param payIds
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/settlement/listOrderIdsByPayIds")
    ServerResponseEntity<List<OrderSettlementSimpleVO>> listOrderIdsByPayIds(@RequestBody List<Long> payIds);
}
