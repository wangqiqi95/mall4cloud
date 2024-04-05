package com.mall4j.cloud.group.controller.gift.app;

import com.mall4j.cloud.api.group.feign.dto.OrderGiftInfoAppDTO;
import com.mall4j.cloud.api.group.feign.dto.OrderGiftReduceAppDTO;
import com.mall4j.cloud.api.group.feign.vo.OrderGiftInfoAppVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.service.OrderGiftBizService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/ma/order_gift")
@Api(tags = "app-下单赠品设置")
public class OrderGiftAppController {
    @Resource
    private OrderGiftBizService orderGiftBizService;

    @PostMapping("/info")
    @ApiOperation(value = "下单送赠品信息", notes = "下单送赠品信息")
    public ServerResponseEntity<OrderGiftInfoAppVO> giftInfo(@RequestBody OrderGiftInfoAppDTO param){
        return orderGiftBizService.info(param);
    }

    @PostMapping("/reduce")
    @ApiOperation(value = "扣减赠品库存", notes = "扣减赠品库存")
    public ServerResponseEntity<Void> reduceStock(@RequestBody List<OrderGiftReduceAppDTO> param){
        return orderGiftBizService.reduce(param);
    }
}
