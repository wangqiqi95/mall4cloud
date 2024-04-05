package com.mall4j.cloud.order.controller.app;

import com.mall4j.cloud.api.biz.feign.LiveStoreClient;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/ua/LiveShopOrderTest")
@Api(tags = "视频号订单测试接口")
public class LiveShopOrderTestController {
    @Autowired
    LiveStoreClient liveStoreClient;

//    @PostMapping("/acceptrefund")
//    @ApiOperation(value = "商家同意退款", notes = "商家同意退款")
//    public ServerResponseEntity<List<Long>> acceptrefund(@RequestParam(value = "refundId", defaultValue = "1") Long refundId) {
//        liveStoreClient.ecaftersaleAcceptrefund(refundId);
//        return ServerResponseEntity.success(null);
//    }
}
