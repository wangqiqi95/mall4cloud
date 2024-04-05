package com.mall4j.cloud.transfer.controller;

import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.transfer.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @luzhengxiang
 * @create 2022-04-05 4:42 PM
 **/
@Slf4j
@RestController("TransferController")
@RequestMapping("/ua/transfer")
@Api(tags = "数据迁移Controller")
public class TransferController {
    @Autowired
    OrderService orderService;
    @Autowired
    UserService userService;
    @Autowired
    RefundOrderService refundOrderService;
    @Autowired
    CouponService couponService;
    @Autowired
    EtoTransferService etoTransferService;


    @GetMapping("/order")
    @ApiOperation(value = "订单数据迁移", notes = "订单数据迁移")
    public ServerResponseEntity<Void> order() {
        orderService.orderTransfer();
        return ServerResponseEntity.success();
    }

    @GetMapping("/refundOrder")
    @ApiOperation(value = "退款订单数据迁移", notes = "退款订单数据迁移")
    public ServerResponseEntity<Void> refundOrder() {
        refundOrderService.refundOrderTransfer();
//        refundOrderService.refundOrderTransfer2();
        return ServerResponseEntity.success();
    }

    @GetMapping("/refundOrder2")
    @ApiOperation(value = "退款订单数据迁移", notes = "退款订单数据迁移")
    public ServerResponseEntity<Void> refundOrder2() {
        refundOrderService.refundOrderTransfer2();
        return ServerResponseEntity.success();
    }

    @GetMapping("/coupon")
    @ApiOperation(value = "优惠券数据迁移", notes = "优惠券数据迁移")
    public ServerResponseEntity<Void> coupon() {
        couponService.couponTransfer();
        return ServerResponseEntity.success();
    }

    @GetMapping("/eto")
    @ApiOperation(value = "eto数据迁移", notes = "eto数据迁移")
    public ServerResponseEntity<Void> eto() {
        etoTransferService.edtoTransfer();
        return ServerResponseEntity.success();
    }

    //先不用了
//    @GetMapping("/user")
//    @ApiOperation(value = "用户数据迁移", notes = "用户数据迁移")
//    public ServerResponseEntity<Void> user() {
//        userService.userTransfer();
//        return ServerResponseEntity.success();
//    }

    @GetMapping("/userSupplement")
    @ApiOperation(value = "补充用户数据迁移", notes = "补充用户数据迁移")
    public ServerResponseEntity<Void> userSupplement() {
        userService.userSupplementTransfer();
        return ServerResponseEntity.success();
    }

    @GetMapping("/all")
    @ApiOperation(value = "数据迁移", notes = "数据迁移")
    public ServerResponseEntity<Void> all() {
        userService.userTransfer();
        orderService.orderTransfer();
        refundOrderService.refundOrderTransfer();
        return ServerResponseEntity.success();
    }

    @GetMapping("/increment")
    @ApiOperation(value = "增量数据迁移", notes = "增量用户数据迁移")
    public ServerResponseEntity<Void> increment() {
        userService.userTransfer();
        return ServerResponseEntity.success();
    }


}
