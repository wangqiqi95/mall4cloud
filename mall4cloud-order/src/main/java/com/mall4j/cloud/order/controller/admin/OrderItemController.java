package com.mall4j.cloud.order.controller.admin;

import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.order.service.OrderItemService;
import com.mall4j.cloud.order.vo.OrderDetailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Pineapple
 * @date 2021/6/9 9:19
 */
@RestController("adminOrderItemController")
@RequestMapping("/mp/order_item")
@Api(tags = "admin-订单项信息")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @GetMapping("/get_order_detail")
    @ApiOperation(value = "查询订单项、退款详情", notes = "根据id查询")
    public ServerResponseEntity<OrderDetailVO> getOrderItemDetail(Long orderId, Long refundId){
        OrderDetailVO orderDetailVO = orderItemService.listDetailByParam(orderId, refundId);
        return ServerResponseEntity.success(orderDetailVO);
    }
}
