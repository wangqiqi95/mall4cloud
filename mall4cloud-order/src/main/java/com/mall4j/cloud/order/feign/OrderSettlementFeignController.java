package com.mall4j.cloud.order.feign;

import com.mall4j.cloud.api.order.feign.OrderSettlementFeignClient;
import com.mall4j.cloud.api.order.vo.OrderSettlementSimpleVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.order.service.OrderSettlementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author lth
 * @Date 2021/7/14 13:33
 */
@RestController
public class OrderSettlementFeignController implements OrderSettlementFeignClient {

    @Autowired
    private OrderSettlementService orderSettlementService;

    @Override
    public ServerResponseEntity<List<OrderSettlementSimpleVO>> listOrderIdsByPayIds(List<Long> payIds) {
        return ServerResponseEntity.success(orderSettlementService.listOrderIdsByPayIds(payIds));
    }
}
