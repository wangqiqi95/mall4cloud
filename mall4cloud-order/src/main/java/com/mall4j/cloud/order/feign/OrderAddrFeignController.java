package com.mall4j.cloud.order.feign;

import com.mall4j.cloud.api.order.feign.OrderAddrFeignClient;
import com.mall4j.cloud.api.order.vo.OrderAddrVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.order.service.OrderAddrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author lth
 * @Date 2021/11/22 9:21
 */
@RestController
public class OrderAddrFeignController implements OrderAddrFeignClient {

    @Autowired
    private OrderAddrService orderAddrService;

    @Override
    public ServerResponseEntity<OrderAddrVO> getOrderAddrByOrderId(Long orderId) {
        OrderAddrVO orderAddrVO = orderAddrService.getByOrderId(orderId);
        return ServerResponseEntity.success(orderAddrVO);
    }
}
