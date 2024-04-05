package com.mall4j.cloud.delivery.controller.admin;

import com.mall4j.cloud.api.delivery.dto.DeliveryOrderDTO;
import com.mall4j.cloud.api.delivery.vo.DeliveryOrderFeignVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.delivery.service.DeliveryOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author lhd on 2020/05/15.
 */
@Controller
@RestController("adminOrderDeliveryController")
@RequestMapping("/mp/order_delivery")
@Api(tags = "物流包裹")
public class OrderDeliveryController {

    @Autowired
    private  DeliveryOrderService deliveryOrderService;

    @GetMapping("/info")
    @ApiOperation(value = "查询订单物流包裹信息", notes = "查询订单物流包裹信息")
    public ServerResponseEntity<List<DeliveryOrderFeignVO>> page(@RequestParam("orderId") Long orderId) throws UnsupportedEncodingException {
        List<DeliveryOrderFeignVO> deliveryOrders = deliveryOrderService.getByDeliveryByOrderId(orderId);
        return ServerResponseEntity.success(deliveryOrders);
    }

    /**
     * 修改订单物流包裹信息
     */
    @PutMapping("/update")
    public ServerResponseEntity<Void> updateOrderDeliveries(@RequestBody List<DeliveryOrderDTO> list) {
        deliveryOrderService.updateOrderDeliveries(list);
        return ServerResponseEntity.success();
    }

}
