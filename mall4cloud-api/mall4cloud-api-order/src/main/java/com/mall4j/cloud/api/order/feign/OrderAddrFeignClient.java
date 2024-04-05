package com.mall4j.cloud.api.order.feign;

import com.mall4j.cloud.api.order.vo.OrderAddrVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author lth
 * @Date 2021/11/22 9:20
 */
@FeignClient(value = "mall4cloud-order",contextId = "orderAddr")
public interface OrderAddrFeignClient {

    /**
     * 根据订单id获取订单地址
     * @param orderId
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/getOrderAddrByOrderId")
    ServerResponseEntity<OrderAddrVO> getOrderAddrByOrderId(@RequestParam("orderId") Long orderId);
}
